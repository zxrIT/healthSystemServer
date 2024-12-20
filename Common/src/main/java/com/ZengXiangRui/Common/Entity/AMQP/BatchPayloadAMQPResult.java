package com.ZengXiangRui.Common.Entity.AMQP;

import lombok.Data;

@Data
public class BatchPayloadAMQPResult<T> {
    public String id;
    public String payload;
    public T data;
}
