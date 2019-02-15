package com.fly.service.impl;

import com.fly.entity.Comment;
import com.fly.dao.CommentMapper;
import com.fly.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fly.xiang
 * @since 2018-10-29
 */
@Service
public class CommentServiceImpl extends BaseServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public void join(Map<String, Object> map, String field) {
        Map<String, Object> joinColumns = new HashMap<>();
        if (map.get(field) == null) {
            return;
        }
//        字段的值
        String linkfieldValue = map.get(field).toString();

        Comment comment = this.getById(linkfieldValue);

        joinColumns.put("id", comment.getId());
        joinColumns.put("content", comment.getContent());
        joinColumns.put("created", comment.getCreated());

        map.put("comment", joinColumns);
    }
}
