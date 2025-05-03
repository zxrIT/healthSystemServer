package com.ZengXiangRui.Elasticsearch;

import com.ZengXiangRui.Common.aop.LoggerAspect;
import com.ZengXiangRui.Common.config.MvcConfig;
import com.ZengXiangRui.Common.exception.GlobalExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@Import({LoggerAspect.class, GlobalExceptionHandler.class, MvcConfig.class})
@EnableElasticsearchRepositories(basePackages = "com.ZengXiangRui.Elasticsearch.repository")
public class ElasticsearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
    }
}
