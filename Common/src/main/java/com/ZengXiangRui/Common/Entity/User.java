package com.ZengXiangRui.Common.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
    private String id;
    private String username;
    private String password;
    private String mobile;
    private String email;
    @TableField("identityCard")
    private String identityCard;
}
