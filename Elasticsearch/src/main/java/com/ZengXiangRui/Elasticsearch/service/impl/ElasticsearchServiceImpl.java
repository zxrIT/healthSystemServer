package com.ZengXiangRui.Elasticsearch.service.impl;

import com.ZengXiangRui.Common.Entity.AMQP.ElasticsearchAMQPParam;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Elasticsearch.entity.ElasticsearchBookKeeping;
import com.ZengXiangRui.Elasticsearch.service.ElasticsearchService;
import com.ZengXiangRui.Elasticsearch.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@SuppressWarnings("all")
@RequiredArgsConstructor
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private final ElasticsearchRepository elasticsearchRepository;

    @LoggerAnnotation(operation = "mysql与es的单一数据同步", dataSource = "elasticsearch")
    public void synchronization(ElasticsearchAMQPParam<ElasticsearchBookKeeping> elasticsearchAMQPParam) {
        ElasticsearchBookKeeping elasticsearchBookKeeping = elasticsearchAMQPParam.getData();
        switch (elasticsearchAMQPParam.getType()) {
            case "delete":
                elasticsearchRepository.delete(elasticsearchBookKeeping);
                break;
            default:
                if (elasticsearchBookKeeping.getTradingHours() == null) {
                    elasticsearchBookKeeping.setTradingHours(DateTimeUtils.getCurrentDateTime());
                }
                elasticsearchRepository.save(elasticsearchBookKeeping);
        }
    }

    @Override
    @LoggerAnnotation(operation = "mysql与es的批量数据同步", dataSource = "elasticsearch")
    public void synchronizationBatch(ElasticsearchAMQPParam<List<ElasticsearchBookKeeping>>
                                             elasticsearchBookKeepingList) {
        elasticsearchRepository.saveAll(elasticsearchBookKeepingList.getData());
    }
}
