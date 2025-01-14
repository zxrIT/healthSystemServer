package com.ZengXiangRui.Common.exception.util.Elasticsearch;

public class ElasticsearchCreateIndexException extends RuntimeException {
    public ElasticsearchCreateIndexException() {
        super();
    }
    public ElasticsearchCreateIndexException(String message) {
        super(message);
    }
}
