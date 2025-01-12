package com.ZengXiangRui.authentication.service.impl;

import com.ZengXiangRui.Common.Entity.AMQP.WechatLogin;
import com.ZengXiangRui.Common.Entity.AMQP.WechatScan;
import com.ZengXiangRui.Common.Utils.Encryption;
import com.ZengXiangRui.Common.Utils.JsonUtil;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Common.exception.util.WechatLoginException;
import com.ZengXiangRui.Common.redis.RedisContent;
import com.ZengXiangRui.authentication.entity.User;
import com.ZengXiangRui.authentication.entity.WeChatUser;
import com.ZengXiangRui.authentication.entity.WechatTokenInfo;
import com.ZengXiangRui.authentication.mapper.UserMapper;
import com.ZengXiangRui.authentication.response.entity.WechatLoginSuccessUserEntity;
import com.ZengXiangRui.authentication.security.TokenManager;
import com.ZengXiangRui.authentication.service.WechatService;
import com.ZengXiangRui.authentication.utils.WechatAttributes;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@SuppressWarnings("all")
public class WechatServiceImpl extends ServiceImpl<UserMapper, User> implements WechatService {
    private final UserMapper userMapper;
    private final TokenManager tokenManager;
    private final StringRedisTemplate stringRedisTemplate;
    private final RabbitTemplate rabbitTemplate;


    @Autowired
    public WechatServiceImpl(UserMapper userMapper, TokenManager tokenManager, StringRedisTemplate stringRedisTemplate,
                             RabbitTemplate rabbitTemplate) {
        this.userMapper = userMapper;
        this.tokenManager = tokenManager;
        this.stringRedisTemplate = stringRedisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    @LoggerAnnotation(operation = "微信验证：请访问 " +
            "http://28a514b7.r11.cpolar.top/authentication/wechat/verification", dataSource = "none")
    public String wechatVerification(String signature, String timestamp, String nonce, String echostr) {
        log.info("接收微信确认消息");
        log.info("signature{}", signature);
        log.info("timestamp{}", timestamp);
        log.info("nonce{}", nonce);
        log.info("echostr{}", echostr);
        return echostr;
    }

    @Override
    @Transactional
    @LoggerAnnotation(operation = "用户微信登录", dataSource = "localhost")
    public String wechatLogin(String code, String state) throws WechatLoginException {
        rabbitTemplate.convertAndSend("zxr.health.wechat.scan.queue", new WechatScan(state, true));
        WechatLoginSuccessUserEntity wechatLoginSuccessUserEntity = new WechatLoginSuccessUserEntity();
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            String tokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                    "appid=" + WechatAttributes.appid +
                    "&secret=" + WechatAttributes.appSecret +
                    "&code=" + code +
                    "&grant_type=authorization_code";
            HttpGet httpGetAccessToken = new HttpGet(tokenUrl);
            String responseResult = null;
            HttpResponse responseAccessToken = httpClient.execute(httpGetAccessToken);
            if (responseAccessToken.getStatusLine().getStatusCode() == 200) {
                responseResult = EntityUtils.toString(responseAccessToken.getEntity(), "UTF-8");
            }
            log.info("获取accessToken结果：{}", responseResult);
            WechatTokenInfo wechatTokenInfo = JsonUtil.jsonToObject(responseResult, WechatTokenInfo.class);
            String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + wechatTokenInfo.getAccess_token() +
                    "&openid=" + wechatTokenInfo.getOpenid() + "&lang=" + WechatAttributes.lang;
            HttpGet httpGetUserInfo = new HttpGet(userInfoUrl);
            HttpResponse responseUserInfo = httpClient.execute(httpGetUserInfo);
            if (responseUserInfo.getStatusLine().getStatusCode() == 200) {
                responseResult = EntityUtils.toString(responseUserInfo.getEntity(), "UTF-8");
            }
            log.info("获取用户个人信息：{}", responseResult);
            WeChatUser wechatUser = JsonUtil.jsonToObject(responseResult, WeChatUser.class);
            User databasesUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, wechatUser.getNickname()));
            if (databasesUser == null) {
                User user = new User();
                user.setUsername(wechatUser.getNickname());
                user.setImageUrl(wechatUser.getHeadimgurl());
                user.setId(Encryption.encryptToMd5(wechatUser.getNickname()));
                userMapper.insert(user);
                String userToken = tokenManager.createToken(user.getId(), user.getUsername());
                String redisKey = "token:" + user.getId();
                stringRedisTemplate.opsForValue().set(redisKey, userToken, RedisContent.TOKEN_TTL, TimeUnit.DAYS);
                wechatLoginSuccessUserEntity.setUsername(wechatUser.getNickname());
                wechatLoginSuccessUserEntity.setToken(userToken);
                wechatLoginSuccessUserEntity.setId(user.getId());
                wechatLoginSuccessUserEntity.setIdentityCard(user.getIdentityCard());
                wechatLoginSuccessUserEntity.setImageUrl(user.getImageUrl());
                wechatLoginSuccessUserEntity.setEmail(user.getEmail());
            } else {
                String userToken = tokenManager.createToken(databasesUser.getId(), databasesUser.getUsername());
                String redisKey = "token:" + databasesUser.getId();
                stringRedisTemplate.opsForValue().set(redisKey, userToken, RedisContent.TOKEN_TTL, TimeUnit.DAYS);
                wechatLoginSuccessUserEntity.setUsername(databasesUser.getUsername());
                wechatLoginSuccessUserEntity.setToken(userToken);
                wechatLoginSuccessUserEntity.setId(databasesUser.getId());
                wechatLoginSuccessUserEntity.setMobile(databasesUser.getMobile());
                wechatLoginSuccessUserEntity.setIdentityCard(databasesUser.getIdentityCard());
                wechatLoginSuccessUserEntity.setImageUrl(databasesUser.getImageUrl());
                wechatLoginSuccessUserEntity.setEmail(databasesUser.getEmail());
            }
        } catch (Exception exception) {
            throw new WechatLoginException(exception.getMessage());
        }
        rabbitTemplate.convertAndSend("zxr.health.wechat.login.queue", new WechatLogin(state, true, wechatLoginSuccessUserEntity));
        return "登录成功";
    }
}

