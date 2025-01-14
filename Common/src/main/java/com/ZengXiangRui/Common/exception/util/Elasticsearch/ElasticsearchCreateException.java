package com.ZengXiangRui.Common.exception.util.Elasticsearch;

public class ElasticsearchCreateException extends RuntimeException {
    public ElasticsearchCreateException(String message) {
        super(message);
    }
    public ElasticsearchCreateException() {
        super();
    }
}
