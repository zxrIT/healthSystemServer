package com.ZengXiangRui.Elasticsearch.service.mapping.impl;

import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Common.exception.util.Elasticsearch.ElasticsearchCreateException;
import com.ZengXiangRui.Elasticsearch.entity.ElasticsearchBookKeeping;
import com.ZengXiangRui.Elasticsearch.response.ElasticsearchResponse;
import com.ZengXiangRui.Elasticsearch.service.mapping.ElasticsearchMappingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@SuppressWarnings("all")
public class ElasticsearchMappingServiceImpl implements ElasticsearchMappingService {

    @Autowired
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    @LoggerAnnotation(operation = "创建索引库", dataSource = "elasticsearch")
    public String createMapping() throws ElasticsearchCreateException {
        if (elasticsearchRestTemplate.indexOps(ElasticsearchBookKeeping.class).exists()) {
            return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "索引库已存在"
            ));
        }
        elasticsearchRestTemplate.indexOps(ElasticsearchBookKeeping.class).create();
        Document mapping = elasticsearchRestTemplate.indexOps(ElasticsearchBookKeeping.class).createMapping();
        elasticsearchRestTemplate.indexOps(ElasticsearchBookKeeping.class).putMapping(mapping);
        return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "索引库添加成功"
        ));
    }
}
