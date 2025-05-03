package com.ZengXiangRui.Elasticsearch.service;

import com.ZengXiangRui.Common.Entity.AMQP.ElasticsearchAMQPParam;
import com.ZengXiangRui.Elasticsearch.entity.ElasticsearchBookKeeping;

import java.util.List;

public interface ElasticsearchService {
    void synchronization(ElasticsearchAMQPParam<ElasticsearchBookKeeping> elasticsearchBookKeeping);

    void synchronizationBatch(ElasticsearchAMQPParam<List<ElasticsearchBookKeeping>> elasticsearchBookKeepingList);
}
