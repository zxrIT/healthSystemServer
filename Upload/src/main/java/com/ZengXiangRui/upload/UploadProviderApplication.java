package com.ZengXiangRui.upload;

import com.ZengXiangRui.Common.aop.LoggerAspect;
import com.ZengXiangRui.Common.config.MvcConfig;
import com.ZengXiangRui.Common.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@EnableDiscoveryClient
@SpringBootApplication
@Import({LoggerAspect.class, GlobalExceptionHandler.class, MvcConfig.class})
public class UploadProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(UploadProviderApplication.class, args);
    }
}
