package com.ZengXiangRui.Common.exception.util.Elasticsearch;

public class ElasticsearchDeleteException extends RuntimeException {
    public ElasticsearchDeleteException(String message) {
        super(message);
    }
    public ElasticsearchDeleteException() {
        super();
    }
}
