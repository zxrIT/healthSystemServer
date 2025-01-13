package com.ZengXiangRui.TodoList.entity;

import com.ZengXiangRui.Common.Entity.todo.Todo;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("todo")
public class TodoEntity extends Todo {
}
