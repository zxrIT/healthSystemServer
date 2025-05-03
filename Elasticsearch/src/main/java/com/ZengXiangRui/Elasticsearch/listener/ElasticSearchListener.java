package com.ZengXiangRui.Elasticsearch.listener;

import com.ZengXiangRui.Common.Entity.AMQP.ElasticsearchAMQPParam;
import com.ZengXiangRui.Elasticsearch.entity.ElasticsearchBookKeeping;
import com.ZengXiangRui.Elasticsearch.service.ElasticsearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@SuppressWarnings("all")
@RequiredArgsConstructor
public class ElasticSearchListener {

    @Autowired
    private final ElasticsearchService elasticsearchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "zxr.healthExchange.elasticsearch1"),
            exchange = @Exchange(name = "zxr.healthExchange.elasticsearch", type = ExchangeTypes.DIRECT),
            key = {"single"}
    ))
    public void mysqlSynchronizationToEs(ElasticsearchAMQPParam<ElasticsearchBookKeeping> elasticsearchBookKeeping) {
        log.info("RabbitMQ信息收到ID为：{}", elasticsearchBookKeeping.getMessageId());
        elasticsearchService.synchronization(elasticsearchBookKeeping);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "zxr.healthExchange.elasticsearch2"),
            exchange = @Exchange(name = "zxr.healthExchange.elasticsearch", type = ExchangeTypes.DIRECT),
            key = {"batch"}
    ))
    public void mysqlSynchronizationBatchToEs(ElasticsearchAMQPParam<List<ElasticsearchBookKeeping>> elasticsearchBookKeeping) {
        log.info("RabbitMQ信息收到ID为：{}", elasticsearchBookKeeping.getMessageId());
        elasticsearchService.synchronizationBatch(elasticsearchBookKeeping);
    }
}
