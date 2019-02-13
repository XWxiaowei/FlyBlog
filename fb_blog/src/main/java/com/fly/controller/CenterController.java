package com.fly.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.entity.Post;
import com.fly.entity.User;
import com.fly.entity.UserCollection;
import com.fly.entity.UserMessage;
import com.fly.shiro.AccountProfile;
import com.fly.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiang.wei on 2018/12/12
 *
 * @author xiang.wei
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class CenterController extends BaseController {


    /**
     * 我发的帖子
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/center")
    public String center(@RequestParam(defaultValue = "1") Integer current,
                         @RequestParam(defaultValue = "10") Integer size) {
        Page<Post> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        log.info("------------->进入个人中心");

        QueryWrapper<Post> wrapper = new QueryWrapper<Post>().eq("user_id", getProfileId())
                .orderByDesc("created");
        IPage<Map<String, Object>> pageData = postService.pageMaps(page, wrapper);
        request.setAttribute("pageData", pageData);

        return "user/center";

    }

    /**
     * 个人收藏分页查询
     *
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/collection")
    public String collection(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        IPage<UserCollection> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Map<String, Object>> pageData = userCollectionService.
                pageMaps(page, new QueryWrapper<UserCollection>()
                        .eq("user_id", getProfileId()).orderByDesc("created"));

        postService.join(pageData, "post_id");
        request.setAttribute("pageData", pageData);

        return "user/collection";
    }

    /**
     * 个人信息
     *
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/message")
    public String message(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        IPage<UserMessage> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Map<String, Object>> pageData = userMessageService.
                pageMaps(page, new QueryWrapper<UserMessage>()
                        .eq("to_user_id", getProfileId()).orderByDesc("created"));

//        关联发送者
        userService.join(pageData, "from_user_id");
//        文章相关消息(评论你文章的提示)
        postService.join(pageData, "post_id");
//        评论你评论的提示
        commentService.join(pageData, "comment_id");
        request.setAttribute("pageData", pageData);

        return "user/message";
    }

    /**
     * 删除消息
     *
     * @param id
     * @param all
     * @return
     */
    @ResponseBody
    @PostMapping("/message/remove")
    public R removeMsg(Long id, boolean all) {
        QueryWrapper<UserMessage> wrapper = new QueryWrapper<UserMessage>()
                .eq("to_user_id", getProfileId())
//                构建条件时写了.eq(!all,"id",id)。当!all时候才加上id
//                的限制
                .eq(!all, "id", id);
        boolean res = userMessageService.remove(wrapper);
        return res ? R.ok(null) : R.failed("删除失败");
    }

    /**
     * 头像上传，文章上传
     * Constant.uploadDir 是要上传的路径
     * Constant.uploadUrl 是我另一个tomcat的项目路径
     * Constant.uploadDir 对应的就是这个Constant.uploadUrl 的访问路径。
     * <p>
     * 可以通过另外部署一个Tomcat或者nginx实现
     * 当然也可以上传的云存储服务器
     *
     * @param file
     * @param type
     * @return
     */
    @ResponseBody
    @PostMapping("/upload")
    public R upload(@RequestParam(value = "file") MultipartFile file,
                    @RequestParam(value = "avatar", name = "type") String type) {
        if (file.isEmpty()) {
            return R.failed("上传失败");
        }
        String fileName = null;
//        获取原始文件名
        String orgName = file.getOriginalFilename();
        log.info("上传文件名为：" + orgName);
//        获取后缀名
        String suffixName = orgName.substring(orgName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);
//        文件上传后的路径
        String filePath = Constant.uploadDir;
        if ("avatar".equalsIgnoreCase(type)) {
            fileName = "/avatar/avatar_" + getProfileId() + suffixName;
        } else if ("post".equalsIgnoreCase(type)) {
            fileName = "post/post_" + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_FORMAT) + suffixName;
        }

        File dest = new File(filePath + fileName);
//        检查目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            //上传文件
            file.transferTo(dest);
            log.info("上传成功之后文件的路径={}", dest.getPath());

            String url = Constant.uploadUrl + fileName;
            log.info("url----->{}", url);

            if ("avatar".equalsIgnoreCase(type)) {
                User current = userService.getById(getProfileId());
                current.setAvatar(url);

                userService.updateById(current);
            }

//            更新shiro的信息
            AccountProfile profile = getProfile();
            profile.setAvatar(url);
            return R.ok(url);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.ok(null);
    }

    @GetMapping("/setting")
    public String setting() {
        User user = userService.getById(getProfileId());
        user.setPassword(null);

        request.setAttribute("user", user);
        return "user/setting";
    }

    @ResponseBody
    @PostMapping("/setting")
    public R postSetting(User user) {
        User tempUser = userService.getById(getProfileId());
        tempUser.setUsername(user.getUsername());
        tempUser.setGender(user.getGender());
        tempUser.setSign(user.getSign());
        tempUser.setMobile(user.getMobile());

        boolean isSucc = userService.updateById(tempUser);
        if (isSucc) {
            //更新shiro的信息
            AccountProfile profile = getProfile();
            profile.setUsername(user.getUsername());
            profile.setGender(user.getGender());
        }

        return isSucc ? R.ok(user) : R.failed("更新失败");
    }

    /**
     * 新消息通知功能
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/message/nums")
    public Object getMessNums() {
        Map<Object, Object> result = new HashMap<>();
        result.put("status", 0);
        result.put("count", 3);
        return result;
    }


    /**
     * 已读消息
     * @return
     */
    @ResponseBody
    @PostMapping("/message/read")
    public Object readMessNums() {
        return R.ok(null);
    }

    /**
     * 重置密码
     *
     * @param nowpass 当前密码
     * @param pass    新密码
     * @return
     */
    @ResponseBody
    @PostMapping("/resetPwd")
    public R restPwd(String nowpass, String pass) {
        //查询用户
        User user = userService.getById(getProfileId());
        if (user == null || !nowpass.equals(user.getPassword())) {
            return R.failed("密码不正确");
        }
        user.setPassword(pass);
        boolean result = userService.updateById(user);
        return R.ok(result);
    }



}
