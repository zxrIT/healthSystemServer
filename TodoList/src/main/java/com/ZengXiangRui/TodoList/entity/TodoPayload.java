package com.ZengXiangRui.TodoList.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TodoPayload {
    private String content;
    private Date createTime;
    private int status;
    private String userId;
}
