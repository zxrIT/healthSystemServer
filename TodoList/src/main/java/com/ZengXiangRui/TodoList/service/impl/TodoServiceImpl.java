package com.ZengXiangRui.TodoList.service.impl;

import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.Encryption;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.Utils.UserContext;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Common.exception.util.TodoListException;
import com.ZengXiangRui.TodoList.entity.TodoEntity;
import com.ZengXiangRui.TodoList.entity.TodoPayload;
import com.ZengXiangRui.TodoList.mapper.TodoMapper;
import com.ZengXiangRui.TodoList.response.TodoListResponse;
import com.ZengXiangRui.TodoList.service.TodoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoServiceImpl extends ServiceImpl<TodoMapper, TodoEntity> implements TodoService {
    private final ThreadLocal<String> emailTitle = ThreadLocal.withInitial(() -> "代办事项");
    private final ThreadLocal<String> emailServerAddress = ThreadLocal.withInitial(() -> "healthSystem@126.com");
    private final TodoMapper todoMapper;
    private final JavaMailSender javaMailSender;

    @Autowired
    public TodoServiceImpl(TodoMapper todoMapper, JavaMailSender javaMailSender) {
        this.todoMapper = todoMapper;
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "添加代办", dataSource = "localhost")
    public String increment(TodoPayload todoPayload) throws TodoListException {
        try {
            TodoEntity todoEntity = new TodoEntity();
            todoEntity.setId(Encryption.encryptToMd5(UserContext.getUserId() + todoPayload.getCreateTime()));
            todoEntity.setUserId(UserContext.getUserId());
            todoEntity.setContent(todoPayload.getContent());
            todoEntity.setStatus(todoPayload.getStatus());
            todoEntity.setCreateTime(todoPayload.getCreateTime());
            todoMapper.insert(todoEntity);
        } catch (Exception exception) {
            throw new TodoListException(exception.getMessage());
        }
        return JsonSerialization.toJson(new TodoListResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "添加成功"
        ));
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "查询代办", dataSource = "localhost")
    public String getTodoList() throws TodoListException {
        List<TodoEntity> todoEntities;
        try {
            todoEntities = todoMapper.selectList(new LambdaQueryWrapper<TodoEntity>().eq(TodoEntity::getUserId,
                            UserContext.getUserId())
                    .orderByDesc(TodoEntity::getCreateTime));
        } catch (Exception exception) {
            throw new TodoListException(exception.getMessage());
        }
        return JsonSerialization.toJson(new TodoListResponse<List<TodoEntity>>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, todoEntities
        ));
    }

    @Override
    public String sendEmail(String email, String content) throws TodoListException {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(emailTitle.get());
            simpleMailMessage.setText(content);
            simpleMailMessage.setTo(email);
            simpleMailMessage.setFrom(emailServerAddress.get());
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            throw new TodoListException(exception.getMessage());
        }
        return JsonSerialization.toJson(new TodoListResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "发送成功"
        ));
    }
}
