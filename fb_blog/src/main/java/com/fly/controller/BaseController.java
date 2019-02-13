package com.fly.controller;

import com.fly.service.CategoryService;
import com.fly.service.CommentService;
import com.fly.service.PostService;
import com.fly.service.UserCollectionService;
import com.fly.service.UserMessageService;
import com.fly.service.UserService;
import com.fly.shiro.AccountProfile;
import com.fly.utils.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xiang.wei on 2018/10/31
 *
 * @author xiang.wei
 */
public class BaseController {
    @Autowired
    HttpServletRequest request;
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    UserCollectionService userCollectionService;
    @Autowired
    UserMessageService userMessageService;
    @Autowired
    CommentService commentService;
    @Autowired
    RedisUtil redisUtil;


    protected AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

    protected Long getProfileId() {
        return getProfile().getId();
    }

}
