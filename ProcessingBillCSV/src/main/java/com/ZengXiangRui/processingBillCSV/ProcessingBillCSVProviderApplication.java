package com.ZengXiangRui.processingBillCSV;

import com.ZengXiangRui.Common.aop.LoggerAspect;
import com.ZengXiangRui.Common.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@EnableDiscoveryClient
@SpringBootApplication
@Import({LoggerAspect.class, GlobalExceptionHandler.class})
public class ProcessingBillCSVProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProcessingBillCSVProviderApplication.class, args);
    }
}
