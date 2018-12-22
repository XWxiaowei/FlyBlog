package com.fly.service;

import com.fly.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jay.xiang
 * @since 2018-10-29
 */
public interface PostService extends BaseService<Post> {

    /**
     * 初始化首页的周评论排行榜
     */
    void initIndexWeekRank();

    /**
     * 给set里的文章评论加1，并且重新union 7天的评论数量
     * @param postId
     */
    void incrZsetValueAndUnionForLastWeekRank(Long postId);

}
