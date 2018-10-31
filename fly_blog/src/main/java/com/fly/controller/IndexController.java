package com.fly.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * Created by xiang.wei on 2018/10/28
 *
 * @author xiang.wei
 */
@Slf4j
@Controller
public class IndexController extends BaseController {

    private static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";


    /**
     * @return
     */
    @GetMapping("/")
    public String index() {
        Page<Post> page = new Page<>();
        page.setCurrent(1);
        page.setSize(10);

        IPage<Map<String, Object>> pageData = postService.pageMaps(page, null);
        //添加关联的用户信息
        userService.join(pageData, "user_id");

        request.setAttribute("pageData",pageData);

        log.info("---------------->这是首页");

        return "index";
    }
}
