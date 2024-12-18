package com.ZengXiangRui.processingBillCSV.AMQPListener;

import com.ZengXiangRui.processingBillCSV.csvProcessor.CSVProcessor;
import com.ZengXiangRui.processingBillCSV.entity.CSVLineObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@SuppressWarnings("all")
public class AMQPListener {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "zxr.health")
    public void listenerSimpleQueue(String message) {
        List<CSVLineObject> csvLineObjectList = CSVProcessor.csvHandler(message);
        rabbitTemplate.convertAndSend("zxr.health.batch.create", "", csvLineObjectList);
    }
}
