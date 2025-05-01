package com.ZengXiangRui.Elasticsearch.service.search.impl;

import com.ZengXiangRui.Common.Utils.UserContext;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Common.exception.util.Elasticsearch.ElasticsearchGetIndexException;
import com.ZengXiangRui.Elasticsearch.service.search.ElasticsearchSearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.ZengXiangRui.Elasticsearch.util.DateUtils.*;

@Service
@Slf4j
@SuppressWarnings("all")
public class ElasticsearchSearchServiceImpl implements ElasticsearchSearchService {
    private final RestHighLevelClient restHighLevelClient;

    @Autowired
    public ElasticsearchSearchServiceImpl(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    @LoggerAnnotation(operation = "查询某一天的账单记录", dataSource = "elasticsearch")
    public String searchDaybook(Date date) {
        try {
            String startTime = formatDate(getStartOfDay(date));
            String endTime = formatDate(getEndOfDay(date));
            SearchRequest searchRequest = new SearchRequest("bill");
            searchRequest.source().query(QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("userId", UserContext.getUserId()))
                    .must(QueryBuilders.rangeQuery("tradingHours")
                            .gte(getStartOfDay(date))
                            .lte(getEndOfDay(date)))
            ).size(10000);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(searchResponse);
            log.debug("查询时间范围: {} 至 {}", startTime, endTime);
        } catch (Exception exception) {
            log.error("查询某一天账单记录失败:{}", exception.getMessage());
            throw new ElasticsearchGetIndexException(exception.getMessage());
        }
        return "success";
    }
}
