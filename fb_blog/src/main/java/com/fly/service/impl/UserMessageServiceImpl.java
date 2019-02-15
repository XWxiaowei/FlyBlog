package com.fly.service.impl;

import com.fly.entity.UserMessage;
import com.fly.dao.UserMessageMapper;
import com.fly.service.UserMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 我的消息 服务实现类
 * </p>
 *
 * @author fly.xiang
 * @since 2018-12-12
 */
@Service
public class UserMessageServiceImpl extends ServiceImpl<UserMessageMapper, UserMessage> implements UserMessageService {

}
