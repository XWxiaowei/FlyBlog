package com.fly.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jay.xiang
 * @since 2018-10-29
 */
@Slf4j
@Controller
public class PostController extends BaseController {

    @GetMapping("/category/{id}")
    public String category(@PathVariable Long id,
                           @RequestParam(defaultValue = "1") Integer current,
                           @RequestParam(defaultValue = "10") Integer size) {
        Page<Post> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Map<String, Object>> pageData = postService.pageMaps(page, new QueryWrapper<Post>()
                .eq("category_id", id)
                .orderByDesc("created"));

//        添加关联的用户信息
        userService.join(pageData, "user_id");
        categoryService.join(pageData, "category_id");

        request.setAttribute("pageData", pageData);
        request.setAttribute("currentCategoryId", id);
        return "category";
    }

    @GetMapping("/post/{id}")
    public String index(@PathVariable Long id) {
        Map<String, Object> post = postService.getMap(new QueryWrapper<Post>().eq("id", id));
        userService.join(post, "user_id");
        Assert.notNull(post, "该文章已被删除");

        request.setAttribute("post", post);
        request.setAttribute("currentCategoryId", post.get("category_id"));
        return "post";
    }

}

