package com.ZengXiangRui.Common.Entity.authentication;

import com.ZengXiangRui.Common.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class WechatLoginUserResponse extends User {
    private String token;
}
