package com.ZengXiangRui.Chat;

import com.ZengXiangRui.Common.aop.LoggerAspect;
import com.ZengXiangRui.Common.config.MvcConfig;
import com.ZengXiangRui.Common.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SuppressWarnings("all")
@EnableScheduling
@SpringBootApplication
@Import({LoggerAspect.class, GlobalExceptionHandler.class, MvcConfig.class})
@EnableDiscoveryClient
public class SpringAIChatApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringAIChatApplication.class, args);
    }
}
