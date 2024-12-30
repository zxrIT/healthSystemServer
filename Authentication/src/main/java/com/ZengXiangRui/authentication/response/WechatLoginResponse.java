package com.ZengXiangRui.authentication.response;

import com.ZengXiangRui.Common.Response.BaseResponse;

public class WechatLoginResponse<T> extends BaseResponse<T> {
    public WechatLoginResponse(int code, String message, T data) {
        super(code, message, data);
    }
}
