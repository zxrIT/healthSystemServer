package com.ZengXiangRui.Elasticsearch.service.impl;

import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.Utils.JsonUtil;
import com.ZengXiangRui.Common.Utils.UserContext;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Common.exception.util.Elasticsearch.ElasticsearchCreateIndexException;
import com.ZengXiangRui.Common.exception.util.Elasticsearch.ElasticsearchDeleteException;
import com.ZengXiangRui.Common.exception.util.Elasticsearch.ElasticsearchGetIndexException;
import com.ZengXiangRui.Common.exception.util.Elasticsearch.ElasticsearchUpdateIndexException;
import com.ZengXiangRui.Elasticsearch.entity.ElasticsearchBookKeeping;
import com.ZengXiangRui.Elasticsearch.mapper.ElasticsearchBookKeepingMapper;
import com.ZengXiangRui.Elasticsearch.response.ElasticsearchResponse;
import com.ZengXiangRui.Elasticsearch.service.ElasticsearchService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@SuppressWarnings("all")
@Slf4j
public class ElasticsearchServiceImpl extends ServiceImpl<ElasticsearchBookKeepingMapper, ElasticsearchBookKeeping>
        implements ElasticsearchService {

    private final ElasticsearchBookKeepingMapper elasticsearchBookKeepingMapper;
    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public ElasticsearchServiceImpl(ElasticsearchBookKeepingMapper elasticsearchBookKeepingMapper,
                                    RestHighLevelClient restHighLevelClient) {
        this.elasticsearchBookKeepingMapper = elasticsearchBookKeepingMapper;
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    @DSTransactional
    @LoggerAnnotation(operation = "批量添加文档", dataSource = "form,elasticsearch")
    @DS("from")
    public String bulkIndexDocument() {
        int totalCount = 0;
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();
        try {
            List<ElasticsearchBookKeeping> elasticsearchBookKeepings =
                    elasticsearchBookKeepingMapper.selectList(new LambdaQueryWrapper<ElasticsearchBookKeeping>()
                            .eq(ElasticsearchBookKeeping::getUserId, UserContext.getUserId()));
            BulkRequest bulkRequest = new BulkRequest();
            elasticsearchBookKeepings.stream().forEach(elasticsearchBookKeeping -> {
                bulkRequest.add(new IndexRequest("bill").id(elasticsearchBookKeeping.getId())
                        .source(JsonSerialization.toJson(elasticsearchBookKeeping), XContentType.JSON)
                );
            });
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                Arrays.stream(bulkResponse.getItems()).forEach(bulkItemResponse -> {
                    if (bulkItemResponse.isFailed()) {
                        failureCount.incrementAndGet();
                        String errorMessage = bulkItemResponse.getFailureMessage();
                        log.error("文档 {} 处理失败: {}", bulkItemResponse.getId(), errorMessage);
                    } else {
                        successCount.getAndIncrement();
                    }
                });
            } else {
                successCount.set(bulkRequest.numberOfActions());
            }
            log.info("批量处理完成 - 总数：{}，成功：{}，失败：{}", totalCount, successCount, failureCount);
            if (failureCount.get() > 0) {
                return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                        BaseResponseUtil.SERVER_ERROR_CODE, BaseResponseUtil.SERVER_ERROR_MESSAGE, "批量创建文档失败"
                ));
            }
        } catch (Exception exception) {
            log.error("批量处理账务数据失败{}", exception.getMessage());
            throw new ElasticsearchCreateIndexException(exception.getMessage());
        }
        return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "批量创建文档成功"
        ));
    }


    @Override
    @DSTransactional
    @LoggerAnnotation(operation = "修改文档", dataSource = "form,elasticsearch")
    @DS("from")
    public String updateIndexDocument(String billId) throws ElasticsearchUpdateIndexException {
        try {
            ElasticsearchBookKeeping elasticsearchBookKeeping =
                    elasticsearchBookKeepingMapper.selectOne(new LambdaQueryWrapper<ElasticsearchBookKeeping>()
                            .eq(ElasticsearchBookKeeping::getUserId, UserContext.getUserId())
                            .eq(ElasticsearchBookKeeping::getId, billId));
            if (elasticsearchBookKeeping == null) {
                log.error("修改文档失败,所对应的账单信息为空");
                return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "修改文档失败,所对应的账单信息为空"
                ));
            }
            GetRequest existsRequest = new GetRequest("bill", billId);
            if (!restHighLevelClient.exists(existsRequest, RequestOptions.DEFAULT)) {
                log.info("文档不存在");
                return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "修改文档失败,文档不存在"
                ));
            }
            UpdateRequest updateRequest = new UpdateRequest("bill", billId);
            updateRequest.doc(
                    "amountOfTransaction", 199.78
            );
            restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (Exception exception) {
            log.error("修改文档失败:{}", exception.getMessage());
            throw new ElasticsearchUpdateIndexException(exception.getMessage());
        }
        return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "修改文档成功"
        ));
    }

    @Override
    @LoggerAnnotation(operation = "删除文档", dataSource = "elasticsearch")
    public String deleteIndexDocument(String billId) {
        try {
            GetRequest existsRequest = new GetRequest("bill", billId);
            if (!restHighLevelClient.exists(existsRequest, RequestOptions.DEFAULT)) {
                log.info("文档不存在");
                return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "删除文档失败,文档不存在"
                ));
            }
            DeleteRequest deleteRequest = new DeleteRequest("bill", billId);
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (Exception exception) {
            log.error("删除文档失败:{}", exception.getMessage());
            throw new ElasticsearchDeleteException(exception.getMessage());
        }
        return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "删除文档成功"
        ));
    }

    @Override
    @DSTransactional
    @LoggerAnnotation(operation = "添加文档", dataSource = "form,elasticsearch")
    @DS("from")
    public String createIndexDocument(String billId) throws ElasticsearchCreateIndexException {
        try {
            ElasticsearchBookKeeping elasticsearchBookKeeping =
                    elasticsearchBookKeepingMapper.selectOne(new LambdaQueryWrapper<ElasticsearchBookKeeping>()
                            .eq(ElasticsearchBookKeeping::getUserId, UserContext.getUserId())
                            .eq(ElasticsearchBookKeeping::getId, billId));
            if (elasticsearchBookKeeping == null) {
                log.error("创建文档失败,所对应的账单信息为空");
                return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "创建文档失败,所对应的账单信息为空"
                ));
            }
            GetRequest getRequest = new GetRequest("bill", elasticsearchBookKeeping.getId());
            if (restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT)) {
                log.info("文档已存在");
                return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "创建文档失败,文档已存在"
                ));
            }
            IndexRequest indexRequest = new IndexRequest("bill").id(elasticsearchBookKeeping.getId());
            indexRequest.source(JsonUtil.objectToJson(elasticsearchBookKeeping), XContentType.JSON);
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception exception) {
            log.error("创建文档失败:{}", exception.getMessage());
            throw new ElasticsearchCreateIndexException(exception.getMessage());
        }
        return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "创建文档成功"
        ));
    }

    @Override
    @LoggerAnnotation(operation = "查询文档", dataSource = "elasticsearch")
    public String getIndexDocument(String billId) throws ElasticsearchGetIndexException {
        ElasticsearchBookKeeping elasticsearchBookKeeping;
        try {
            GetRequest existsRequest = new GetRequest("bill", billId);
            if (!restHighLevelClient.exists(existsRequest, RequestOptions.DEFAULT)) {
                log.info("文档不存在");
                return JsonSerialization.toJson(new ElasticsearchResponse<String>(
                        BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "查询文档失败,文档不存在"
                ));
            }
            GetRequest getRequest = new GetRequest("bill", billId);
            GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            String getResponseDataJson = getResponse.getSourceAsString();
            elasticsearchBookKeeping = JsonUtil.jsonToObject(getResponseDataJson, ElasticsearchBookKeeping.class);
        } catch (Exception exception) {
            log.error("查询文档失败:{}", exception.getMessage());
            throw new ElasticsearchGetIndexException(exception.getMessage());
        }
        return JsonSerialization.toJson(new ElasticsearchResponse<ElasticsearchBookKeeping>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, elasticsearchBookKeeping
        ));
    }
}
