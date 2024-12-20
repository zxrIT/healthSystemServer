package com.ZengXiangRui.BookKeepingProvider.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfiguration {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue fanoutQueueBatchCreate() {
        return new Queue("batch.create.status");
    }

    @Bean
    public Queue fanoutQueueProcessor() {
        return new Queue("processor.status");
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("zxr.health.batch.create.status");
    }

    @Bean
    public Binding fanoutBindingBatchCreate() {
        return BindingBuilder.bind(fanoutQueueBatchCreate()).to(fanoutExchange());
    }

    @Bean
    public Binding fanoutBindingProcessor() {
        return BindingBuilder.bind(fanoutQueueProcessor()).to(fanoutExchange());
    }
}
