package com.fly.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.entity.Post;
import com.fly.dao.PostMapper;
import com.fly.search.dto.PostDTO;
import com.fly.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author fly.xiang
 * @since 2018-10-29
 */
@Service
public class PostServiceImpl extends BaseServiceImpl<PostMapper, Post> implements PostService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PostMapper postMapper;

    @Override
    public void join(Map<String, Object> map, String field) {
        Map<String, Object> joinColumns = new HashMap<>();

        if (map.get(field) == null) {
            return;
        }

        //字段的值
        String linkfieldValue = map.get(field).toString();

        Post post = this.getById(linkfieldValue);

        joinColumns.put("id", post.getId());
        joinColumns.put("title", post.getTitle());
        joinColumns.put("created", DateUtil.formatDate(post.getCreated()));

        map.put("post", joinColumns);
    }

    @Override
    public void initIndexWeekRank() {
//        查库获取最近7天的所有文章
        List<Post> last7DayPosts = this.list(new QueryWrapper<Post>()
                .ge("created", DateUtil.offsetDay(new Date(), -7).toJdkDate())
                .gt("comment_count", 0)
                .select("id, title, user_id, comment_count, view_count, created"));
//        然后把文章的评论数量作为有序集合的分数，文章id作为ID存储到zset中。
        for (Post post : last7DayPosts) {
            String key = "day_rank" + DateUtil.format(post.getCreated(), DatePattern.PURE_DATE_PATTERN);
//            设置有效期,7天之内有效
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

//            缓存文章到set中，评论数量作为排行标准
            redisUtil.zSet(key, post.getId(), post.getCommentCount());
            //设置有效期
            redisUtil.expire(key, expireTime);
//            缓存文章基本信息（hash结构）
            this.hashCachePostIdAndTitle(post);

        }

//        7天阅读相加
        this.zUnionAndStroreLast7DaysForWeekRand();

    }

    @Override
    public void incrZsetValueAndUnionForLastWeekRank(Long postId) {
        String dayRank = "day_rank" + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
//        文章阅读加一
        redisUtil.zIncrementScore(dayRank, postId, 1);
        this.hashCachePostIdAndTitle(this.getById(postId));

//      重新union最近7天
        this.zUnionAndStroreLast7DaysForWeekRand();
    }

    @Override
    public PostDTO findPostDTOById(long postId) {
        return postMapper.findPostDTOById(postId);
    }


    @Override
    public IPage<PostDTO> findPostDTOByPage(Page<PostDTO> page, String keyword) {
        return postMapper.findPostDTOByPage(page, keyword);
    }

    /**
     * hash结构缓存文章标题和id
     *
     * @param post
     */
    private void hashCachePostIdAndTitle(Post post) {
        boolean isExist = redisUtil.hasKey("rank_post_" + post.getId());
        if (!isExist) {
            long between = DateUtil.between(new Date(), post.getCreated(), DateUnit.DAY);
            long expireTime = (7 - between) * 24 * 60 * 60;

//            缓存文章基本信息
            redisUtil.hset("rank_post_" + post.getId(), "post:id", post.getId(), expireTime);
            redisUtil.hset("rank_post_" + post.getId(), "post:title", post.getTitle(), expireTime);
        }
    }

    /**
     * 把最近7天的文章评论数量统计一下
     * 用于首页的7天评论排行榜
     */
    public void zUnionAndStroreLast7DaysForWeekRand() {
        String prifix = "day_rank";

        List<String> keys = new ArrayList<>();
        String key = prifix + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);

        for (int i = -7; i < 0; i++) {
            Date date = DateUtil.offsetDay(new Date(), i).toJdkDate();
            keys.add(prifix + DateUtil.format(date, DatePattern.PURE_DATE_PATTERN));
        }

        redisUtil.zUnionAndStore(key, keys, "last_week_rank");

    }
}
