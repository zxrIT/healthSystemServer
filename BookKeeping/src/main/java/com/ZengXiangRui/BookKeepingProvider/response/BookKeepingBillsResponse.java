package com.ZengXiangRui.BookKeepingProvider.response;

import com.ZengXiangRui.Common.Response.BaseResponse;

public class BookKeepingBillsResponse<T> extends BaseResponse<T> {
    public BookKeepingBillsResponse(int code, String message, T data) {
        super(code, message, data);
    }
}
