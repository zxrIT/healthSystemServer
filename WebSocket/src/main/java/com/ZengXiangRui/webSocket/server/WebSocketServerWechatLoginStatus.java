package com.ZengXiangRui.webSocket.server;

import com.ZengXiangRui.Common.Entity.AMQP.WechatLogin;
import com.ZengXiangRui.Common.Entity.AMQP.WechatScan;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint("/wechat/login/status/{loginId}")
public class WebSocketServerWechatLoginStatus {
    static Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("loginId") String loginId) {
        sessions.put(loginId, session);
        log.info("========websocket=========");
        log.info("与服务器建立连接");
        log.info("session:{}", session);
        log.info("==========================");
    }

    @OnClose
    public void onClose(Session session, @PathParam("loginId") String loginId) {
        log.info("========websocket=========");
        log.info("客户端关闭连接");
        log.info("==========================");
        sessions.remove(loginId);
    }

    @RabbitListener(queues = "zxr.health.wechat.scan.queue")
    public void sendScanStatus(WechatScan wechatScan) throws IOException {
        log.info("对{}发送消息{}", wechatScan.getLoginId(), wechatScan.getScanStatus());
        Session session = sessions.get(wechatScan.getLoginId());
        if (session != null) {
            session.getBasicRemote().sendText(JsonSerialization.toJson(wechatScan));
        }
    }

    @RabbitListener(queues = "zxr.health.wechat.login.queue")
    public void sendLoginStatus(WechatLogin wechatLogin) throws IOException {
        log.info("对{}发送消息{},数据为{}", wechatLogin.getLoginId(), wechatLogin.getScanStatus(), wechatLogin.getWechatLoginUserResponse());
        Session session = sessions.get(wechatLogin.getLoginId());
        if (session != null) {
            session.getBasicRemote().sendText(JsonSerialization.toJson(wechatLogin));
        }
    }
}
