package com.ZengXiangRui.authentication.service;

import com.ZengXiangRui.authentication.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface NotThirdPartyService extends IService<User> {
    String login(String username, String password);
}
