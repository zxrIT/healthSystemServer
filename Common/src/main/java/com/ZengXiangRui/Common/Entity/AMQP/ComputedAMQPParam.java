package com.ZengXiangRui.Common.Entity.AMQP;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComputedAMQPParam {
    private long messageId;
    private String userId;
}
