package com.ZengXiangRui.authentication.service.impl;

import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.authentication.entity.User;
import com.ZengXiangRui.authentication.mapper.UserMapper;
import com.ZengXiangRui.authentication.response.LogoutResponse;
import com.ZengXiangRui.authentication.service.LogoutService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogoutServiceImpl extends ServiceImpl<UserMapper, User> implements LogoutService {
    private final UserMapper userMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public LogoutServiceImpl(UserMapper userMapper, StringRedisTemplate stringRedisTemplate) {
        this.userMapper = userMapper;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "验证token的合法性", dataSource = "redis")
    public String verificationToken(String userId, String token) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
        if (user == null) {
            return JsonSerialization.toJson(new LogoutResponse<Boolean>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, false
            ));
        }
        String redisKey = "token:" + userId;
        String redisToken = stringRedisTemplate.opsForValue().get(redisKey);
        if (redisToken == null) {
            return JsonSerialization.toJson(new LogoutResponse<Boolean>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, false
            ));
        }
        return JsonSerialization.toJson(new LogoutResponse<Boolean>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, true
        ));
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "用户退出登录", dataSource = "redis")
    public String logout(String userId) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getId, userId));
        if (user == null) {
            return JsonSerialization.toJson(new LogoutResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "用户不存在，请重新登录"
            ));
        }
        String redisKey = "token:" + userId;
        stringRedisTemplate.delete(redisKey);
        return JsonSerialization.toJson(new LogoutResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "退出成功"
        ));
    }
}
