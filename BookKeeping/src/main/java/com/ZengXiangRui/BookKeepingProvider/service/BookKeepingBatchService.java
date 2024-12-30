package com.ZengXiangRui.BookKeepingProvider.service;

import com.ZengXiangRui.BookKeepingProvider.entity.BookKeepingBill;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface BookKeepingBatchService extends IService<BookKeepingBill> {
    Boolean batchCreate(List<BookKeepingBill> bookKeepingBills, String userId);
}
