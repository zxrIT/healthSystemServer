package com.ZengXiangRui.authentication.amqp;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue queueScan() {
        return new Queue("zxr.health.wechat.scan.queue");
    }

    @Bean
    Queue queueWechatLogin() {
        return new Queue("zxr.health.wechat.login.queue");
    }
}
