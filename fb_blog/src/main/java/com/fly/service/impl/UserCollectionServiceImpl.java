package com.fly.service.impl;

import com.fly.entity.UserCollection;
import com.fly.dao.UserCollectionMapper;
import com.fly.service.UserCollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 个人收藏 服务实现类
 * </p>
 *
 * @author fly.xiang
 * @since 2018-12-12
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements UserCollectionService {

}
