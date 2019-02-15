package com.fly.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.fly.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fly.shiro.AccountProfile;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fly.xiang
 * @since 2018-10-29
 */
public interface UserService extends BaseService<User> {


    /**
     * @param email
     * @param password
     * @return
     */
    AccountProfile login(String email, String password);
    /**
     * 获取关联用户
     * @param pageData
     * @param linkfield
     */
    void join(IPage<Map<String, Object>> pageData, String linkfield);


    /**
     * @param user
     * @return
     */
    R register(User user);

}
