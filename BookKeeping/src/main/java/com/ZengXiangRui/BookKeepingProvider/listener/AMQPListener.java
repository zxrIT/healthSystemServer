package com.ZengXiangRui.BookKeepingProvider.listener;

import com.ZengXiangRui.BookKeepingProvider.entity.BookKeepingBill;
import com.ZengXiangRui.BookKeepingProvider.service.BookKeepingBatchService;
import com.ZengXiangRui.Common.Entity.AMQP.BatchCreateAMQPResult;
import com.ZengXiangRui.Common.Entity.AMQP.BatchPayloadAMQPResult;
import com.ZengXiangRui.Common.Utils.Encryption;
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

    @Autowired
    private BookKeepingBatchService bookKeepingBatchService;

    @RabbitListener(queues = "batch.create")
    public void listenerSimpleQueue(BatchCreateAMQPResult<List<BookKeepingBill>> batchCreateAMQPResultMessage) {
        String batchUniqueId = batchCreateAMQPResultMessage.getId();
        Boolean batchCreateStatus = bookKeepingBatchService.batchCreate(batchCreateAMQPResultMessage.getData(), batchCreateAMQPResultMessage.getUserId());
        BatchPayloadAMQPResult<Boolean> batchCreateAMQPResult = new BatchPayloadAMQPResult<>();
        batchCreateAMQPResult.setPayload("batchCreate");
        batchCreateAMQPResult.setId(batchUniqueId);
        if (batchCreateStatus) {
            batchCreateAMQPResult.setData(true);
        } else {
            batchCreateAMQPResult.setData(false);
        }
        rabbitTemplate.convertAndSend("zxr.health.batch.create.status", "", batchCreateAMQPResult);
    }
}
