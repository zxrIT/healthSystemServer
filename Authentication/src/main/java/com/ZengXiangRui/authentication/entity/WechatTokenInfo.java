package com.ZengXiangRui.authentication.entity;

import lombok.Data;

@Data
public class WechatTokenInfo {
    private String access_token;
    private String refresh_token;
    private String openid;
    private long expires_in;
    private String scope;
}
