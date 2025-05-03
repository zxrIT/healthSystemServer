package com.ZengXiangRui.Computed.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("money")
public class MoneyEntity {
    @TableField("userId")
    private String userId;

    private double income;

    private double disburse;

    private double surplus;
}
