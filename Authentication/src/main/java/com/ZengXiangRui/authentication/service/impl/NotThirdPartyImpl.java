package com.ZengXiangRui.authentication.service.impl;

import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.Encryption;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Common.redis.RedisContent;
import com.ZengXiangRui.authentication.entity.User;
import com.ZengXiangRui.authentication.mapper.UserMapper;
import com.ZengXiangRui.authentication.response.PasswordLoginResponse;
import com.ZengXiangRui.authentication.response.entity.PasswordLoginSuccessUserEntity;
import com.ZengXiangRui.authentication.security.TokenManager;
import com.ZengXiangRui.authentication.service.NotThirdPartyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@SuppressWarnings("all")
public class NotThirdPartyImpl extends ServiceImpl<UserMapper, User> implements NotThirdPartyService {
    private final UserMapper userMapper;
    private final TokenManager tokenManager;
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public NotThirdPartyImpl(UserMapper userMapper, TokenManager tokenManager, StringRedisTemplate stringRedisTemplate) {
        this.userMapper = userMapper;
        this.tokenManager = tokenManager;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "用户登录", dataSource = "localhost")
    public String login(String username, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null) {
            return JsonSerialization.toJson(new PasswordLoginResponse<>(
                    BaseResponseUtil.FORBIDDEN_CODE, BaseResponseUtil.FORBIDDEN_MESSAGE,
                    "用户不存在，请注册后再进行登录"
            ));
        }
        String md5Password = Encryption.encryptToMd5(password);
        if (!Objects.equals(user.getPassword(), md5Password)) {
            return JsonSerialization.toJson(new PasswordLoginResponse<>(
                    BaseResponseUtil.FORBIDDEN_CODE, BaseResponseUtil.FORBIDDEN_MESSAGE,
                    "用户名或密码错误,请重新输入"
            ));
        }
        String userToken = tokenManager.createToken(user.getId(), user.getUsername());
        stringRedisTemplate.opsForValue().set(user.getId(), userToken, RedisContent.CACHE_TTL, TimeUnit.MINUTES);
        PasswordLoginSuccessUserEntity passwordLoginSuccessUserEntity = new PasswordLoginSuccessUserEntity();
        passwordLoginSuccessUserEntity.setUsername(user.getUsername());
        passwordLoginSuccessUserEntity.setToken(userToken);
        passwordLoginSuccessUserEntity.setId(user.getId());
        passwordLoginSuccessUserEntity.setMobile(user.getMobile());
        passwordLoginSuccessUserEntity.setIdentityCard(user.getIdentityCard());
        return JsonSerialization.toJson(new PasswordLoginResponse<PasswordLoginSuccessUserEntity>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, passwordLoginSuccessUserEntity
        ));
    }
}
