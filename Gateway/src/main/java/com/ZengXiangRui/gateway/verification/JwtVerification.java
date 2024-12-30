package com.ZengXiangRui.gateway.verification;

import com.ZengXiangRui.Common.exception.util.ForbiddenException;
import com.ZengXiangRui.gateway.entity.TokenUserInformation;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JwtVerification {
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public JwtVerification(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private String SecretKey = "zxr-health";

    public String parseJwt(String token) {
        Jwt jwt;
        try {
            jwt = Jwts.parser().setSigningKey(SecretKey).parse(token);
        } catch (Exception exception) {
            throw new ForbiddenException("您的登录状态已过期请重新登录");
        }
        String jwtString = jwt.getBody().toString();
        String[] splitString = jwt.getBody().toString().substring(1, jwtString.length() - 1).trim().split(",");
        TokenUserInformation tokenUserInformation = new TokenUserInformation();
        for (String string : splitString) {
            if (string.contains("id=")) {
                tokenUserInformation.setId(string.split("id=")[1]);
            } else if (string.contains("name=")) {
                tokenUserInformation.setUsername(string.split("name=")[1]);
            }
        }
        String stringToken = stringRedisTemplate.opsForValue().get("token:" + tokenUserInformation.getId());
        if (!Objects.equals(stringToken, token)) {
            throw new ForbiddenException("您的登录状态已过期请重新登录");
        }
        return tokenUserInformation.getId();
    }
}
