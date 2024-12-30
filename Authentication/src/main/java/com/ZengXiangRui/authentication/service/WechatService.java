package com.ZengXiangRui.authentication.service;

import com.ZengXiangRui.authentication.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface WechatService extends IService<User> {
    String wechatVerification(String signature, String timestamp, String nonce, String echostr);

    String wechatLogin(String code, String state);
}
