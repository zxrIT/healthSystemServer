package com.ZengXiangRui.Computed.response;

import com.ZengXiangRui.Common.Response.BaseResponse;

public class ComputedResponse<T> extends BaseResponse<T> {
    public ComputedResponse(int code, String message, T data) {
        super(code, message, data);
    }
}
