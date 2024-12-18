package com.ZengXiangRui.BookKeepingProvider.listener;

import com.ZengXiangRui.BookKeepingProvider.entity.BookKeepingBill;
import com.ZengXiangRui.BookKeepingProvider.service.BookKeepingBatchService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@SuppressWarnings("all")
public class AMQPListener {

    @Autowired
    private BookKeepingBatchService bookKeepingBatchService;

    @RabbitListener(queues = "batch.create")
    public void listenerSimpleQueue(List<BookKeepingBill> bookKeepingBills) {
        Boolean batchCreateStatus = bookKeepingBatchService.batchCreate(bookKeepingBills);
    }
}
