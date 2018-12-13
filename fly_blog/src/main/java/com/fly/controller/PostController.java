package com.fly.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.entity.Category;
import com.fly.entity.Post;
import com.fly.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
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

    /**
     * 文章分类查看
     *
     * @param id
     * @param current
     * @param size
     * @return
     */
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

    /**
     * 文章首页
     *
     * @param id
     * @return
     */
    @GetMapping("/post/{id}")
    public String index(@PathVariable Long id) {
        Map<String, Object> post = postService.getMap(new QueryWrapper<Post>().eq("id", id));
        userService.join(post, "user_id");
        Assert.notNull(post, "该文章已被删除");

        request.setAttribute("post", post);
        request.setAttribute("currentCategoryId", post.get("category_id"));
        return "post";
    }

    /**
     * 博客回显
     *
     * @return
     */
    @GetMapping("/user/post")
    public String getPost(String id) {

        Post post = new Post();
        if (!StringUtils.isEmpty(id)) {
            post = postService.getById(Long.valueOf(id));
        }
        request.setAttribute("pid", id);
        request.setAttribute("post", post);

        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>()
                .orderByDesc("order_num"));
        request.setAttribute("categories", categoryList);
        return "post/add";

    }


    /**
     * 发表文章
     * @param post
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @PostMapping("/user/post")
    public R postArticle(@Valid Post post, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.failed(bindingResult.getFieldError().getDefaultMessage());
        }
//        新增文章
        if (post.getId() == null) {
            post.setUserId(getProfileId());

            post.setModified(new Date());
            post.setCreated(new Date());
            post.setCommentCount(0);
            post.setEditMode(Constant.EDIT_HTML_MODEL);
            post.setLevel(0);
            post.setRecommend(false);
            post.setViewCount(0);
            post.setVoteDown(0);
            post.setVoteUp(0);
            post.setStatus(Constant.NORMAL_STATUS);

        } else {
            Post tempPost = postService.getById(post.getId());
            if(tempPost.getUserId().equals(getProfileId())) {
                return R.failed("不是自己的帖子");
            }
        }
        postService.saveOrUpdate(post);
        // TODO: 2018/12/13 给所有订阅人发送消息

        return R.ok(post.getId());
    }
}

