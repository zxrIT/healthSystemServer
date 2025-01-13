package com.ZengXiangRui.TodoList;

import com.ZengXiangRui.Common.aop.LoggerAspect;
import com.ZengXiangRui.Common.config.MvcConfig;
import com.ZengXiangRui.Common.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
@MapperScan("com.ZengXiangRui.TodoList.mapper")
@Import({LoggerAspect.class, GlobalExceptionHandler.class, MvcConfig.class})
public class TodoListProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(TodoListProviderApplication.class, args);
    }
}
