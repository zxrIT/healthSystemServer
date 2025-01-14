package com.ZengXiangRui.Elasticsearch.controller;

import com.ZengXiangRui.Elasticsearch.service.ElasticsearchService;
import com.ZengXiangRui.Elasticsearch.service.mapping.ElasticsearchMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/elasticsearch")
public class ElasticsearchController {
    private final ElasticsearchService elasticsearchService;
    private final ElasticsearchMappingService elasticsearchMappingService;

    @Autowired
    public ElasticsearchController(ElasticsearchService elasticsearchService, ElasticsearchMappingService elasticsearchMappingService) {
        this.elasticsearchService = elasticsearchService;
        this.elasticsearchMappingService = elasticsearchMappingService;
    }

    @PutMapping("/create/mapping")
    public String createMapping() {
        return elasticsearchMappingService.createMapping();
    }

    @PostMapping("/create/document/{billId}")
    public String createDocument(@PathVariable String billId) {
        return elasticsearchService.createIndexDocument(billId);
    }

    @GetMapping("/get/document/{billId}")
    public String getDocument(@PathVariable String billId) {
        return elasticsearchService.getIndexDocument(billId);
    }

    @PutMapping("/update/document/{billId}")
    public String updateDocument(@PathVariable String billId) {
        return elasticsearchService.updateIndexDocument(billId);
    }

    @DeleteMapping("/delete/document/{billId}")
    public String deleteDocument(@PathVariable String billId) {
        return elasticsearchService.deleteIndexDocument(billId);
    }

    @PostMapping("/bulk/document")
    public String bulkDocument() {
        return elasticsearchService.bulkIndexDocument();
    }
}
