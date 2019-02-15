package com.fly.controller;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fly.entity.Post;
import com.fly.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 * 用户主页
 *
 * @author fly.xiang
 * @since 2018-10-29
 */
@Controller
public class UserController  extends BaseController{

    @RequestMapping("/u/{id}")
    public String home(@PathVariable Long id) {
        User user = userService.getById(id);
        user.setPassword(null);

//        30天内的文章
        Date date30Before = DateUtil.offsetDay(new Date(), -30).toJdkDate();

        List<Post> postList = postService.list(new QueryWrapper<Post>()
                .eq("user_id", id)
                .eq("created", date30Before)
                .orderByDesc("created"));

        // TODO: 2018/12/13 用户动作
        request.setAttribute("user", user);
        request.setAttribute("post", postList);
        return "user/home";
    }
}

