package com.ZengXiangRui.webSocket.server;

import com.ZengXiangRui.Common.Entity.AMQP.BatchPayloadAMQPResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/upload/csv/status")
@Slf4j
public class WebSocketServerUploadCsvEndPoint {
    static Map<String, Session> sessions = new ConcurrentHashMap<>();
    static Map<String, Boolean> processorMap = new ConcurrentHashMap<>();
    static Map<String, Boolean> batchCreateMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        log.info("========websocket=========");
        log.info("与服务器建立连接");
        log.info("session:{}", session);
        log.info("==========================");
        sessions.put(session.getId(), session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        if (!sessions.containsKey(message)) {
            sessions.put(message, session);
        }
        try {
            if (!processorMap.containsKey(message) || !batchCreateMap.containsKey(message)) {
                sessions.get(message).getBasicRemote().sendText("error");
                return;
            }
            if (processorMap.get(message) == true && batchCreateMap.get(message) == true) {
                sessions.get(message).getBasicRemote().sendText("yes");
            }
            if (processorMap.get(message) == false || batchCreateMap.get(message) == false) {
                sessions.get(message).getBasicRemote().sendText("no");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        log.info("========websocket=========");
        log.info("客户端关闭连接");
        log.info("==========================");
        sessions.remove(session.getId());
    }

    @RabbitListener(queues = {"batch.create.status", "processor.status"})
    public void getQueueBatchStatus(BatchPayloadAMQPResult<Boolean> batchPayloadAMQPResult) {
        if (batchPayloadAMQPResult.getPayload().equals("processor")) {
            processorMap.put(batchPayloadAMQPResult.getId(), batchPayloadAMQPResult.getData());
        } else {
            batchCreateMap.put(batchPayloadAMQPResult.getId(), batchPayloadAMQPResult.getData());
        }
    }
}
