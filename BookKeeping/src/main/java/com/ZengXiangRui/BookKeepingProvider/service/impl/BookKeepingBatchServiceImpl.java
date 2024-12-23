package com.ZengXiangRui.BookKeepingProvider.service.impl;

import com.ZengXiangRui.BookKeepingProvider.entity.BookKeepingBill;
import com.ZengXiangRui.BookKeepingProvider.mapper.BookKeepingBillMapper;
import com.ZengXiangRui.BookKeepingProvider.service.BookKeepingBatchService;
import com.ZengXiangRui.Common.Utils.ErrorLogger;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@SuppressWarnings("all")
@Slf4j
public class BookKeepingBatchServiceImpl extends ServiceImpl<BookKeepingBillMapper, BookKeepingBill>
        implements BookKeepingBatchService {

    @Autowired
    private BookKeepingBillMapper bookKeepingBillMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final Integer batch = 1000;

    @Override
    @DS("master")
    @LoggerAnnotation(operation = "批量新增", dataSource = "master")
    @DSTransactional
    public Boolean batchCreate(List<BookKeepingBill> bookKeepingBills) {
        try {
            this.saveOrUpdateBatch(bookKeepingBills, batch);
            Set<String> keys = stringRedisTemplate.keys("book:keeping:*");
            stringRedisTemplate.delete(keys);
        } catch (Exception exception) {
            ErrorLogger.Log(this.getClass(), exception.getMessage());
            return false;
        }
        return true;
    }
}
