package com.ZengXiangRui.Elasticsearch.service.mapping.impl;

import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Common.exception.util.Elasticsearch.ElasticsearchCreateException;
import com.ZengXiangRui.Elasticsearch.content.ElasticsearchMappingContent;
import com.ZengXiangRui.Elasticsearch.response.ElasticsearchResponse;
import com.ZengXiangRui.Elasticsearch.service.mapping.ElasticsearchMappingService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
@Slf4j
public class ElasticsearchMappingServiceImpl implements ElasticsearchMappingService {
    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public ElasticsearchMappingServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    @LoggerAnnotation(operation = "创建索引库", dataSource = "elasticsearch")
    public String createMapping() throws ElasticsearchCreateException {
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest("bill");
            if (restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT)) {
                log.info("索引库已存在");
                return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "索引库已存在"
                ));
            }
            CreateIndexRequest createIndexRequest = new CreateIndexRequest("bill");
            createIndexRequest.source(ElasticsearchMappingContent.createMappingContent, XContentType.JSON);
            restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        } catch (Exception exception) {
            log.error("创建索引库失败{}", exception.getMessage());
            throw new ElasticsearchCreateException(exception.getMessage());
        }
        return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "索引库创建成功"
        ));
    }
}
