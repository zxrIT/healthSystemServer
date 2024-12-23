package com.ZengXiangRui.Common.Entity.AMQP;

import lombok.Data;

@Data
public class BatchCreateAMQPResult<T> {
    public String id;
    public String userId;
    public T data;
}
