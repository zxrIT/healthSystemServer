package com.ZengXiangRui.Common.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;
}
