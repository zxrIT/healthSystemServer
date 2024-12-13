package com.ZengXiangRui.BookKeepingProvider;

import com.ZengXiangRui.Common.aop.LoggerAspect;
import com.ZengXiangRui.Common.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.ZengXiangRui.BookKeepingProvider.mapper")
@Import({LoggerAspect.class, GlobalExceptionHandler.class})
public class BookKeepingProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(BookKeepingProviderApplication.class, args);
    }
}
