package com.ZengXiangRui.Computed.entity;

import lombok.Data;

@Data
public class MonthlySpendingDTO {
    private Integer month;                 // 月份
    private Double investmentAmount;       // 投资理财
    private Double transferAmount;         // 转账红包
    private Double foodAmount;             // 餐饮美食
    private Double transportAmount;        // 交通出行
    private Double shoppingAmount;         // 电商消费
    private Double totalAmount;            // 总消费
}