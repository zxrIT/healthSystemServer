package com.ZengXiangRui.BookKeepingProvider.service.impl;

import com.ZengXiangRui.BookKeepingProvider.entity.BookKeepingBill;
import com.ZengXiangRui.BookKeepingProvider.mapper.BookKeepingBillMapper;
import com.ZengXiangRui.BookKeepingProvider.redis.RedisIdWorker;
import com.ZengXiangRui.BookKeepingProvider.service.BookKeepingBatchService;
import com.ZengXiangRui.Common.Entity.AMQP.ElasticsearchAMQPParam;
import com.ZengXiangRui.Common.Utils.ErrorLogger;
import com.ZengXiangRui.Common.Utils.UserContext;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@SuppressWarnings("all")
@Slf4j
@RequiredArgsConstructor
public class BookKeepingBatchServiceImpl extends ServiceImpl<BookKeepingBillMapper, BookKeepingBill>
        implements BookKeepingBatchService {

    private final String globallyUniqueRedisKey = "bookKeeping:globally:Unique:redis:key";

    @Autowired
    private final RedisIdWorker redisIdWorker;

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private final BookKeepingBillMapper bookKeepingBillMapper;

    @Autowired
    private final StringRedisTemplate stringRedisTemplate;

    private static final Integer batch = 10000;

    @Override
    @DS("master")
    @LoggerAnnotation(operation = "批量新增", dataSource = "master")
    @DSTransactional
    public Boolean batchCreate(List<BookKeepingBill> bookKeepingBills, String userId) {
        try {
            this.saveOrUpdateBatch(bookKeepingBills, batch);
            rabbitTemplate.convertAndSend("zxr.healthExchange.elasticsearch", "batch",
                    new ElasticsearchAMQPParam<List<BookKeepingBill>>("create", redisIdWorker.nextId(globallyUniqueRedisKey),
                            bookKeepingBills));
            Set<String> keys = stringRedisTemplate.keys("book:keeping:user" + userId + ":*");
            stringRedisTemplate.delete(keys);
        } catch (Exception exception) {
            ErrorLogger.Log(this.getClass(), exception.getMessage());
            return false;
        }
        return true;
    }
}
