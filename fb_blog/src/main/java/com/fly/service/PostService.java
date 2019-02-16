package com.fly.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.entity.Post;
import com.fly.search.dto.PostDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fly.xiang
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

    /**
     * 查找文章
     * @param postId
     * @return
     */
    PostDTO findPostDTOById(long postId);

    /**
     * @param page
     * @param keyword
     * @return
     */
     IPage<PostDTO> findPostDTOByPage(Page<PostDTO> page, String keyword);
}
