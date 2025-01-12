package com.ZengXiangRui.authentication.response;

import com.ZengXiangRui.Common.Response.BaseResponse;

public class LogoutResponse<T> extends BaseResponse<T> {
    public LogoutResponse(int code, String message, T data) {
        super(code, message, data);
    }
}
