package com.ZengXiangRui.Common.Utils;

import java.nio.charset.StandardCharsets;

public class Encryption {
    public static String encryptToMd5(String code) {
        return org.springframework.util.DigestUtils.md5DigestAsHex(
                code.getBytes(StandardCharsets.UTF_8)
        );
    }
}
