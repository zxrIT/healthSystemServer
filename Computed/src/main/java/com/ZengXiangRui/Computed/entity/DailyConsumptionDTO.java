package com.ZengXiangRui.Computed.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DailyConsumptionDTO {
    private Date date;              // 消费日期
    private Double totalSpending;   // 当日支出总额（负数）
    private Integer transactionCount; // 当日交易笔数
    private String consumptionTypes; // 消费类型（逗号分隔）
}