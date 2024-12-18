package com.ZengXiangRui.processingBillCSV.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("zxr.health.batch.create");
    }

    @Bean
    public Queue fantQueue() {
        return new Queue("batch.create");
    }

    @Bean
    public Binding fantBinding() {
        return BindingBuilder.bind(fantQueue()).to(fanoutExchange());
    }
}
