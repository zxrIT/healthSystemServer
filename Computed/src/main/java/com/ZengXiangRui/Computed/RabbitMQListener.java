package com.ZengXiangRui.Computed;

import com.ZengXiangRui.Common.Entity.AMQP.ComputedAMQPParam;
import com.ZengXiangRui.Common.Entity.AMQP.ComputedAMQTimeParam;
import com.ZengXiangRui.Computed.service.ComputedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@SuppressWarnings("all")
@RequiredArgsConstructor
public class RabbitMQListener {

    @Autowired
    private ComputedService computedService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "zxr.health.money"),
            exchange = @Exchange(name = "zxr.health.other", type = ExchangeTypes.DIRECT),
            key = {"money"}
    ))
    public void setTotalSpending(@Payload ComputedAMQPParam computedAMQPParam) {
        log.info("RabbitMQ信息收到ID为：{}", computedAMQPParam.getMessageId());
        computedService.setTotalSpending(computedAMQPParam.getUserId());
    }


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "zxr.health.time"),
            exchange = @Exchange(name = "zxr.health.other", type = ExchangeTypes.DIRECT),
            key = {"time"}
    ))
    public void setUploadTime(@Payload ComputedAMQTimeParam computedAMQTimeParam) {
        log.info("RabbitMQ信息收到ID为：{}", computedAMQTimeParam.getMessageId());
        computedService.setUploadTime(computedAMQTimeParam.getUserId(), computedAMQTimeParam.getType()
                , computedAMQTimeParam.getUploadTime());
    }
}
