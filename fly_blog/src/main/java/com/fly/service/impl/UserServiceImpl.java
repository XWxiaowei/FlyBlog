package com.fly.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fly.entity.User;
import com.fly.dao.UserMapper;
import com.fly.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public void join(IPage<Map<String, Object>> pageData, String linkfield) {
        List<Map<String, Object>> records = pageData.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            for (Map<String, Object> record : records) {
                String userId = record.get(linkfield).toString();
                User user = this.getById(userId);


                Map<String, Object> author = new HashMap<>();
                author.put("username", user.getUsername());
                author.put("email", user.getEmail());
                author.put("avatar", user.getAvatar());
                author.put("id", user.getId());

                record.put("author", author);

            }
        }
    }
}
