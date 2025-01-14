package com.ZengXiangRui.Elasticsearch.response;

import com.ZengXiangRui.Common.Response.BaseResponse;

public class ElasticsearchResponse<T> extends BaseResponse<T> {
    public ElasticsearchResponse(int code, String message, T data) {
        super(code, message, data);
    }
}
