package com.fly.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fly.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fly.search.dto.PostDTO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author fly.xiang
 * @since 2018-10-29
 */
public interface PostMapper extends BaseMapper<Post> {
    PostDTO findPostDTOById(long postId);

    IPage<PostDTO> findPostDTOByPage(Page<PostDTO> page, @Param("keyword") String keyword);


}
