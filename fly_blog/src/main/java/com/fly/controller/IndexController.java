package com.fly.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.entity.Post;
import com.fly.entity.User;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xiang.wei on 2018/10/28
 *
 * @author xiang.wei
 */
@Slf4j
@Controller
public class IndexController extends BaseController {

    private static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";
    @Autowired
    private Producer producer;


    @GetMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-store,no-cache");
        response.setContentType("image/jpeg");

//        生成文字验证码
        String text = producer.createText();
//        生成图片验证码
        BufferedImage image = producer.createImage(text);
//        把验证码存到shiro的session中
        SecurityUtils.getSubject().getSession().setAttribute(KAPTCHA_SESSION_KEY, text);
//        设置输出流
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);

    }

    /**
     * @return
     */
    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") Integer current,
                        @RequestParam(defaultValue = "10") Integer size) {

        Page<Post> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Map<String, Object>> pageData = postService.pageMaps(page, null);
        //添加关联的用户信息
        userService.join(pageData, "user_id");

        request.setAttribute("pageData", pageData);

        log.info("---------------->" + pageData.getRecords());
        log.info("---------------->" + page.getPages());

//        置顶文章(取5条)
        List<Map<String, Object>> levelPosts = postService.listMaps(new QueryWrapper<Post>()
                .orderByDesc("level").last("limit 5"));
        userService.join(levelPosts, "user_id");
        categoryService.join(levelPosts, "category_id");

        request.setAttribute("levelPosts", levelPosts);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public R doLogin(String email, String password, ModelMap model) {
        if (StringUtils.isAnyBlank(email, password)) {
            return R.failed("用户名或密码不能为空");
        }

        UsernamePasswordToken token = new UsernamePasswordToken(email, SecureUtil.md5(password));

        try {

            //尝试登陆，将会调用realm的认证方法
            SecurityUtils.getSubject().login(token);

        } catch (AuthenticationException e) {
            if (e instanceof UnknownAccountException) {
                return R.failed("用户不存在");
            } else if (e instanceof LockedAccountException) {
                return R.failed("用户被禁用");
            } else if (e instanceof IncorrectCredentialsException) {
                return R.failed("密码错误");
            } else {
                return R.failed("用户认证失败");
            }
        }

        return R.ok("登录成功");

    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    @ResponseBody
    public R doRegister(User user, String captcha) {
        String kaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute(KAPTCHA_SESSION_KEY);
        if (!kaptcha.equalsIgnoreCase(captcha)) {
            System.out.println(kaptcha + "----" + captcha);
            return R.failed("验证码不正确");
        }

        R r = userService.register(user);
        return r;
    }

    @GetMapping("/user/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }

    /**
     * TODO 要使用redis有序列表实现
     * 思路：
     * 项目启动初始化最近7天发表文章的评论数量，
     * 发表评论给对应点的文章添加comment-count。同时设置有效期。
     *
     * 命令使用：
     * 1、ZINCRBY rank:20181020 5 1
     * 2、ZRANGE rank:20181020 0 -1 withscores
     * 3、ZREVRANGE rank:20181220 0 -1 withsroces
     * 4、ZINCRBY rank:20181019 99 1
     * 统计
     * 5、ZUNIONSTORE rank:last_week 7 rank:20181019 rank:20181020 rank:20181021 weights 1 1 1
     * 6、ZREVRANGE rank:last_week 0 -1 withscores
     * 设定有效期
     * 7、ttl rank:last_week
     * 8、expire rank:last_week 60*60*24*7
     * @return
     */
    @ResponseBody
    @GetMapping("/post/hosts")
    public R hostPost() {
        Set<ZSetOperations.TypedTuple> lastWeekRank = redisUtil.getZSetRank("last_week_rank", 0, 6);
        List<Map<String,Object>> hostPost = new ArrayList<>();
        if (lastWeekRank == null) {
            return R.ok(hostPost);
        }
        for (ZSetOperations.TypedTuple typedTuple : lastWeekRank) {
            Map<String, Object> map = new HashMap<>();
            map.put("commnet_count", typedTuple.getScore());
            map.put("id", redisUtil.hget("rank_post_" + typedTuple.getValue(), "post:id"));
            map.put("title", redisUtil.hget("rank_post_" + typedTuple.getValue(), "post:title"));
            hostPost.add(map);

        }
        return R.ok(hostPost);
    }

}

