package com.ZengXiangRui.processingBillCSV.AMQPListener;

import com.ZengXiangRui.Common.Entity.AMQP.BatchCreateAMQPResult;
import com.ZengXiangRui.Common.Entity.AMQP.BatchPayloadAMQPResult;
import com.ZengXiangRui.Common.Utils.ErrorLogger;
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
    public void listenerSimpleQueue(BatchCreateAMQPResult<String> batchCreateAMQPResultMessage) {
        System.out.println(batchCreateAMQPResultMessage.getData());
        System.out.println(batchCreateAMQPResultMessage.getId());
        String batchUniqueId = batchCreateAMQPResultMessage.getId();
        BatchCreateAMQPResult<List<CSVLineObject>> batchCreateAMQPResultToDataBase = new BatchCreateAMQPResult<>();
        BatchPayloadAMQPResult<Boolean> batchCreateAMQPResultStatus = new BatchPayloadAMQPResult<Boolean>();
        batchCreateAMQPResultStatus.setPayload("processor");
        try {
            List<CSVLineObject> csvLineObjectList = CSVProcessor.csvHandlerAli(batchCreateAMQPResultMessage.getData());
            batchCreateAMQPResultToDataBase.setId(batchUniqueId);
            batchCreateAMQPResultToDataBase.setData(csvLineObjectList);
            rabbitTemplate.convertAndSend("zxr.health.batch.create", "", batchCreateAMQPResultToDataBase);
            batchCreateAMQPResultStatus.setId(batchUniqueId);
            batchCreateAMQPResultStatus.setData(true);
        } catch (Exception exception) {
            ErrorLogger.Log(AMQPListener.class, exception.getMessage());
            batchCreateAMQPResultStatus.setId(batchUniqueId);
            batchCreateAMQPResultStatus.setData(false);
        } finally {
            rabbitTemplate.convertAndSend("zxr.health.batch.create.status", "", batchCreateAMQPResultStatus);
        }
    }
}
