package com.ZengXiangRui.TodoList.service;

import com.ZengXiangRui.TodoList.entity.TodoEntity;
import com.ZengXiangRui.TodoList.entity.TodoPayload;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;

public interface TodoService extends IService<TodoEntity> {
    String increment(@RequestBody TodoPayload todoPayload);

    String getTodoList();

    String sendEmail(String email, String content);
}
