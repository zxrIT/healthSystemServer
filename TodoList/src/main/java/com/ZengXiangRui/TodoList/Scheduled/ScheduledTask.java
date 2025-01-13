package com.ZengXiangRui.TodoList.Scheduled;

import com.ZengXiangRui.TodoList.mapper.TodoMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    private final TodoMapper todoMapper;

    @Autowired
    public ScheduledTask(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private void deleteAllTodo() {
        todoMapper.delete(null);
    }
}