package com.ZengXiangRui.authentication.service;

import com.ZengXiangRui.authentication.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface LogoutService extends IService<User> {
    String verificationToken(String userId, String token);

    String logout(String userId);
}
