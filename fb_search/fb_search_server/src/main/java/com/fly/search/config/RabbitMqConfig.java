package com.fly.search.config;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;

/**
 * Created by xiang.wei on 2019/2/16
 *
 * @author xiang.wei
 */
@Configuration
public class RabbitMqConfig {

    //    队列名称
    public final static String ES_QUEUE = "es_queue";
    public final static String ES_EXCHANGE = "es_exchange";
    public final static String ES_BIND_KEY = "es_index_message";


    /**
     * 声明队列
     * @return
     */
    @Bean
    public Queue exQueue() {
        return new Queue(ES_QUEUE);
    }

    /**
     * 声明交换机
     * @return
     */
    @Bean
    DirectExchange exchange() {
        return new DirectExchange(ES_EXCHANGE);
    }

    @Bean
    Binding bindingExchangeMessage(Queue exQueue, DirectExchange exchange) {
        return BindingBuilder.bind(exQueue).to(exchange).with(ES_BIND_KEY);
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
