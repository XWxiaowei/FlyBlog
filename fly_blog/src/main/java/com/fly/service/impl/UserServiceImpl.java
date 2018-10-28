package com.fly.service.impl;

import com.fly.entity.User;
import com.fly.dao.UserMapper;
import com.fly.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
