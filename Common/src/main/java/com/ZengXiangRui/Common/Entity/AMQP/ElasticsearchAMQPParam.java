package com.ZengXiangRui.Common.Entity.AMQP;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticsearchAMQPParam<T> {
    public String type;
    public long messageId;
    public T data;
}
