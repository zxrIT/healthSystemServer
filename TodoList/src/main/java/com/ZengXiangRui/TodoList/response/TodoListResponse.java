package com.ZengXiangRui.TodoList.response;

import com.ZengXiangRui.Common.Response.BaseResponse;

public class TodoListResponse<T> extends BaseResponse<T> {
    public TodoListResponse(int code, String message, T data) {
        super(code, message, data);
    }
}
