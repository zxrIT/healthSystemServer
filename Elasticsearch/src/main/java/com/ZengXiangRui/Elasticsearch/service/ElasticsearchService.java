package com.ZengXiangRui.Elasticsearch.service;

import com.ZengXiangRui.Elasticsearch.entity.ElasticsearchBookKeeping;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ElasticsearchService extends IService<ElasticsearchBookKeeping> {
    String createIndexDocument(String billId);
    String getIndexDocument(String billId);
    String updateIndexDocument(String billId);
    String deleteIndexDocument(String billId);
    String bulkIndexDocument();
}
