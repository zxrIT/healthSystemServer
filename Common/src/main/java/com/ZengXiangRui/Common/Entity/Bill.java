package com.ZengXiangRui.Common.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Bill {
    @TableId
    private String id;

    @TableField("transactionClassification")
    private String transactionClassification;

    @TableField("counterparty")
    private String counterparty;

    @TableField("productDescription")
    private String productDescription;

    @TableField("directionOfTrade")
    private int directionOfTrade;

    @TableField("amountOfTransaction")
    private double amountOfTransaction;

    @TableField("modeOfTransaction")
    private String modeOfTransaction;

    @TableField("transactionStatus")
    private String transactionStatus;

    @TableField(value = "tradeOrderNumber")
    private String tradeOrderNumber;

    @TableField("remarks")
    private String remarks;

    @TableField("tradingHours")
    private Date tradingHours;

    @TableField("userId")
    private String userId;
}
