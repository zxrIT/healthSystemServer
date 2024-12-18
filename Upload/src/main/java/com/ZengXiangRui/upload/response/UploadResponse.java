package com.ZengXiangRui.upload.response;

import com.ZengXiangRui.Common.Response.BaseResponse;

public class UploadResponse<T> extends BaseResponse<T> {
    public UploadResponse(int code, String message, T data) {
        super(code, message, data);
    }
}
