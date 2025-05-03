package com.ZengXiangRui.Computed.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("uploadTime")
public class UploadTimeEntity {
    @TableField("userId")
    private String userId;
    @TableField("alipay")
    private Date alipay;
    @TableField("wechat")
    private Date wechat;
}
