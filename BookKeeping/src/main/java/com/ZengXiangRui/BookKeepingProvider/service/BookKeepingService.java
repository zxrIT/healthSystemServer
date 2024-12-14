package com.ZengXiangRui.BookKeepingProvider.service;

import com.ZengXiangRui.BookKeepingProvider.entity.BookKeepingBill;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BookKeepingService extends IService<BookKeepingBill> {
    String getBookKeeping(int page, int quantity);

    String createBookKeeping(BookKeepingBill bookKeepingBill);

    String updateBookKeeping(BookKeepingBill bookKeepingBill);

    String deleteBookKeeping(BookKeepingBill bookKeepingBill);
}
