package com.fly.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.entity.Category;
import com.fly.entity.Comment;
import com.fly.entity.Post;
import com.fly.entity.UserCollection;
import com.fly.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
 * @author fly.xiang
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
        return "post/category";
    }

    /**
     * 文章首页
     *
     * @param id
     * @return
     */
    @GetMapping("/post/{id}")
    public String index(@PathVariable Long id,
                        @RequestParam(defaultValue = "1") Integer current,
                        @RequestParam(defaultValue = "10") Integer size) {
        Map<String, Object> post = postService.getMap(new QueryWrapper<Post>().eq("id", id));
        userService.join(post, "user_id");
        categoryService.join(post, "category_id");

        Assert.notNull(post, "该文章已被删除");

        request.setAttribute("post", post);
        request.setAttribute("currentCategoryId", post.get("category_id"));

        Page<Comment> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Map<String, Object>> pageData = commentService.pageMaps(page, new QueryWrapper<Comment>()
                .eq("post_id", id)
                .orderByDesc("created"));

        userService.join(pageData, "user_id");
        commentService.join(pageData, "parent_id");

        request.setAttribute("pageData", pageData);
        return "post/index";
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
     *
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
            if (!tempPost.getUserId().equals(getProfileId())) {
                return R.failed("不是自己的帖子");
            }
        }
        postService.saveOrUpdate(post);
        // TODO: 2018/12/13 给所有订阅人发送消息

        return R.ok(post.getId());
    }


    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/user/post/delete")
    public R postDelete(Long id) {
        Post post = postService.getById(id);

        Assert.isTrue(post != null, "该帖子已被删除");

        Long profileId = getProfileId();
        if (post.getUserId().equals(profileId)) {
            return R.failed("不是删除非自己的帖子");
        }

        postService.removeById(id);

        // 同时删除所有的相关收藏
        userCollectionService.removeByMap(MapUtil.of("post_id", id));
//      同时删除所有的相关评论
        commentService.removeByMap(MapUtil.of("post_id", id));

        return R.ok(null);
    }

    /**
     * 判断是否收藏
     *
     * @param postId
     * @return
     */
    @ResponseBody
    @PostMapping("/user/post/collection/find")
    public R collectionFind(String postId) {
        int count = userCollectionService.count(new QueryWrapper<UserCollection>()
                .eq("post_id", postId)
                .eq("user_id", getProfileId()));

        return R.ok(MapUtil.of("collection", count > 0));
    }

    /**
     * 收藏
     *
     * @param postId
     * @return
     */
    @ResponseBody
    @RequestMapping("/user/post/collection/add")
    public R collectionAdd(Long postId) {
        Post post = postService.getById(postId);
        Assert.isTrue(post != null, "该帖子已被删除");

        int count = userCollectionService.count(new QueryWrapper<UserCollection>()
                .eq("post_id", post.getId())
                .eq("user_id", getProfileId()));
        if (count > 0) {
            return R.failed("你已收藏该贴");
        }
        UserCollection userCollection = new UserCollection();
        userCollection.setPostId(postId);
        userCollection.setUserId(getProfileId());
        userCollection.setCreated(new Date());
        userCollection.setModified(new Date());
        userCollection.setPostUserId(post.getUserId());

        userCollectionService.save(userCollection);
        return R.ok(MapUtil.of("collection", true));
    }

    /**
     * 取消收藏
     *
     * @param postId
     * @return
     */
    @ResponseBody
    @PostMapping("/user/post/collection/remove")
    public R collectionRemove(String postId) {

        Post post = postService.getById(Long.valueOf(postId));

        Assert.isTrue(post != null, "该帖子已被删除");

        boolean hasRemove = userCollectionService.remove(new QueryWrapper<UserCollection>()
                .eq("post_id", postId)
                .eq("user_id", getProfileId()));

        return R.ok(hasRemove);
    }

    /**
     * 保存评论
     * @param comment
     * @param bindingResult
     * @return
     */
    @ResponseBody
    @PostMapping("/user/post/comment")
    public R commentAdd(@Valid Comment comment, BindingResult bindingResult) {
        Post post = postService.getById(comment.getPostId());
        Assert.isTrue(post != null, "该帖子已被删除");

        comment.setUserId(getProfileId());
        comment.setCreated(new Date());
        comment.setModified(new Date());
        comment.setStatus(Constant.NORMAL_STATUS);

//        评论数量加一
        post.setCommentCount(post.getCommentCount() + 1);
        postService.saveOrUpdate(post);
//        更新首页排版的评论数量
        postService.incrZsetValueAndUnionForLastWeekRank(post.getId());

        // TODO 记录动作

        // TODO 通知作者
        commentService.save(comment);
        return R.ok(null);
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/user/post/comment/delete")
    public R commentDel(Long id) {

        Comment comment = commentService.getById(id);

        Assert.isTrue(comment!= null, "该评论已被删除");

        if(!comment.getUserId().equals(getProfileId())) {
            return R.failed("删除失败！");
        }

        commentService.removeById(id);

        return R.ok(null);
    }

}

