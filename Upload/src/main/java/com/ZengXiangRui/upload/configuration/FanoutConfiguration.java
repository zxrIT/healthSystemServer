package com.ZengXiangRui.upload.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfiguration {
    @Bean
    public FanoutExchange fanoutExchangeWeichat() {
        return new FanoutExchange("zxr.HealthExchange.weichat.csv");
    }

    @Bean
    public Queue fanoutQueueWeichat() {
        return new Queue("zxr.Health.weichat");
    }

    @Bean
    public Binding fanoutBindingWeichat() {
        return BindingBuilder.bind(fanoutQueueWeichat()).to(fanoutExchangeWeichat());
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("zxr.HealthExchange.ali.csv");
    }

    @Bean
    public Queue fanoutQueue() {
        return new Queue("zxr.health");
    }

    @Bean
    public Binding fanoutBinding() {
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
