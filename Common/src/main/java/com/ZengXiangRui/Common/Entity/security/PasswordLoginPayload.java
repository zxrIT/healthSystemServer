package com.ZengXiangRui.Common.Entity.security;

import lombok.Data;

@Data
public class PasswordLoginPayload {
    private String username;
    private String password;
}
