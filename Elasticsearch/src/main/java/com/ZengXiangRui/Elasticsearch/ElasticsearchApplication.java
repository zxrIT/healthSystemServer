package com.ZengXiangRui.Elasticsearch;

import com.ZengXiangRui.Common.aop.LoggerAspect;
import com.ZengXiangRui.Common.config.MvcConfig;
import com.ZengXiangRui.Common.exception.GlobalExceptionHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.ZengXiangRui.Elasticsearch.mapper")
@Import({LoggerAspect.class, GlobalExceptionHandler.class, MvcConfig.class})
public class ElasticsearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }
}
