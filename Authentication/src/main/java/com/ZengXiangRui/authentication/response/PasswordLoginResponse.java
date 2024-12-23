package com.ZengXiangRui.authentication.response;

import com.ZengXiangRui.Common.Response.BaseResponse;

public class PasswordLoginResponse<T> extends BaseResponse<T> {
    public PasswordLoginResponse(int code, String message, T data) {
        super(code, message, data);
    }
}
