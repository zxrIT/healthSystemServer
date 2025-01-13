package com.ZengXiangRui.TodoList.controller;

import com.ZengXiangRui.TodoList.entity.TodoPayload;
import com.ZengXiangRui.TodoList.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoListController {
    private final TodoService todoService;

    @Autowired
    public TodoListController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/increment")
    private String increment(@RequestBody TodoPayload todoPayload) {
        return todoService.increment(todoPayload);
    }

    @GetMapping("/getTodoList")
    private String getTodoList() {
        return todoService.getTodoList();
    }

    @GetMapping("/send/todo/email/{email}/{content}")
    private String sendTodoEmail(@PathVariable String email, @PathVariable String content) {
        return todoService.sendEmail(email, content);
    }
}
