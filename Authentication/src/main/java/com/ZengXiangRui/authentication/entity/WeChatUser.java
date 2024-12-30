package com.ZengXiangRui.authentication.entity;

import lombok.Data;

import java.util.List;

@Data
public class WeChatUser {
    private String openid;
    private String nickname;
    private String headimgurl;
    private Integer sex;
    private String province;
    private String city;
    private String country;
    private String language;
    private List<String> privilege;
}
