package com.fly.search.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fly.search.config.RabbitMqConfig;
import com.fly.search.dto.PostMqIndexMessage;
import com.fly.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 监听异步消息队列
 * 更新搜索内容
 * Created by xiang.wei on 2019/2/16
 *
 * @author xiang.wei
 */
@Slf4j
@Component
@RabbitListener(queues = RabbitMqConfig.ES_QUEUE)
public class HandlerMessage {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    SearchService searchService;

    @RabbitHandler
    public void handler(String content) {

        try {
            PostMqIndexMessage message = objectMapper.readValue(content, PostMqIndexMessage.class);
            switch (message.getType()) {
                case PostMqIndexMessage.CREATE:
                case PostMqIndexMessage.UPDATE:
                    searchService.createOrUpdateIndex(message);
                    break;
                case PostMqIndexMessage.REMOVE:
                    searchService.removeIndex(message);
                    break;
                default:
                    log.warn("没有找到对应的消息类型，请注意！！！，-----》{}", content);
                    break;
            }

        } catch (IOException e) {
            log.error("这是内容---->{}",content);
            log.error("处理HandlerMessage失败--->", e);

        }

    }
}
