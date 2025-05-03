package com.ZengXiangRui.Common.Entity.AMQP;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComputedAMQTimeParam {
    private long messageId;
    private String userId;
    private Date uploadTime;
    private String type;
}
