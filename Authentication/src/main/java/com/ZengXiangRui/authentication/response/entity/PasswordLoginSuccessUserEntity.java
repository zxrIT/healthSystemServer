package com.ZengXiangRui.authentication.response.entity;

import com.ZengXiangRui.authentication.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class PasswordLoginSuccessUserEntity extends User {
    private String token;
}
