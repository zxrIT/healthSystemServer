package com.ZengXiangRui.webSocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class WebSocketProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSocketProviderApplication.class, args);
    }
}
