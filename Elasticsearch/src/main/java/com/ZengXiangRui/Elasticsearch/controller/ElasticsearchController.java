package com.ZengXiangRui.Elasticsearch.controller;

import com.ZengXiangRui.Elasticsearch.service.mapping.ElasticsearchMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@SuppressWarnings("all")
@RequiredArgsConstructor
@RequestMapping("/elasticsearch")
public class ElasticsearchController {

    @Autowired
    private final ElasticsearchMappingService elasticsearchMappingService;


    @PutMapping("/create/mapping")
    public String createMapping() {
        return elasticsearchMappingService.createMapping();
    }
}
