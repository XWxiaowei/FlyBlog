package com.fly.controller;

import com.fly.service.CategoryService;
import com.fly.service.PostService;
import com.fly.service.UserService;
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

    
}
