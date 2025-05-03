package com.ZengXiangRui.BookKeepingProvider.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@SuppressWarnings("all")
public class RedisIdWorker {
    private final static long BEGIN_TIMESTAMP = 1640995200L;
    private final static int COUNT_BITS = 32;
    
    @Autowired
    private final StringRedisTemplate stringRedisTemplate;

    public long nextId(String keyPrefix) {
        LocalDateTime now = LocalDateTime.now();
        long nowSeconds = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSeconds - BEGIN_TIMESTAMP;
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long incrementCount = stringRedisTemplate
                .opsForValue().increment("icr:" + keyPrefix + ":" + date);
        return timestamp << COUNT_BITS | incrementCount;
    }
}
