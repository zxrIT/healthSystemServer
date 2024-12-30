package com.ZengXiangRui.Common.Entity.AMQP;

import com.ZengXiangRui.Common.Entity.User;
import com.ZengXiangRui.Common.Entity.authentication.WechatLoginUserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WechatLogin {
    private String loginId;
    private Boolean scanStatus;
    private WechatLoginUserResponse wechatLoginUserResponse;
}
