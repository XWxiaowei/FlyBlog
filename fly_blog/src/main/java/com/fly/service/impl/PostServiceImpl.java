package com.fly.service.impl;

import cn.hutool.core.date.DateUtil;
import com.fly.entity.Post;
import com.fly.dao.PostMapper;
import com.fly.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jay.xiang
 * @since 2018-10-29
 */
@Service
public class PostServiceImpl extends BaseServiceImpl<PostMapper, Post> implements PostService {
    @Override
    public void join(Map<String, Object> map, String field) {
        Map<String, Object> joinColumns = new HashMap<>();

        if(map.get(field) == null) {
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

}
