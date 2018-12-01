package com.fly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fly.entity.User;
import com.fly.dao.UserMapper;
import com.fly.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fly.shiro.AccountProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
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
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public AccountProfile login(String email, String password) {
        log.info("-------------->进入用户登录判断，获取用户信息步骤");

        User user = this.getOne(new QueryWrapper<User>().eq("email", email));
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new IncorrectCredentialsException("密码错误");
        }

//        更新最后登录时间
        user.setLasted(new Date());
        user.updateById();

        AccountProfile profile = new AccountProfile();

        BeanUtils.copyProperties(user, profile);

//        把通知和私信数量查出来

        return profile;
    }

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
