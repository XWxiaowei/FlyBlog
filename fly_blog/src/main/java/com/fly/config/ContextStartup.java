package com.fly.config;

import com.fly.service.CategoryService;
import com.fly.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 *
 * 博客分类信息是在项目启动之后初始化到上下文，这里涉及到ApplicationRunner接口
 * ApplicationRunner接口是在容器启动成功之后的最后一步回调（类似开机自启动）
 * Created by xiang.wei on 2018/12/4
 *
 * @author xiang.wei
 */
@Slf4j
@Order(100)
@Component
public class ContextStartup implements ApplicationRunner,ServletContextAware {

    private ServletContext servletContext;

    @Autowired
    CategoryService categoryService;
    @Autowired
    PostService postService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        servletContext.setAttribute("categorys", categoryService.list(null));

        log.info("ContextStartup------------>加载categorys");

//        初始化首页的周评论排行榜
        postService.initIndexWeekRank();

    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
