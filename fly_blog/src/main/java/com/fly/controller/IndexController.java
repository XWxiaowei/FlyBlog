package com.fly.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by xiang.wei on 2018/10/28
 *
 * @author xiang.wei
 */
@Slf4j
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        log.info("---------------->这是首页");
        return "index";
    }
}
