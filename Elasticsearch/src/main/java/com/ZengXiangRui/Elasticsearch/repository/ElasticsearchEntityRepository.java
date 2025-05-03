package com.ZengXiangRui.Elasticsearch.repository;

import com.ZengXiangRui.Elasticsearch.entity.ElasticsearchBookKeeping;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticsearchEntityRepository extends ElasticsearchRepository<ElasticsearchBookKeeping, String> {
}
