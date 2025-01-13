package com.ZengXiangRui.Common.Entity.todo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Todo {
    private String id;
    private String content;
    @TableField("createTime")
    private Date createTime;
    @TableField("userId")
    private String userId;
    private int status;
}
