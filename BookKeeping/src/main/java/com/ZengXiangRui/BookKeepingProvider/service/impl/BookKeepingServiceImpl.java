package com.ZengXiangRui.BookKeepingProvider.service.impl;

import com.ZengXiangRui.BookKeepingProvider.entity.BookKeepingBill;
import com.ZengXiangRui.BookKeepingProvider.mapper.BookKeepingBillMapper;
import com.ZengXiangRui.BookKeepingProvider.response.BookKeepingBillsResponse;
import com.ZengXiangRui.BookKeepingProvider.service.BookKeepingService;
import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.Encryption;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Common.exception.util.DataBaseException;
import com.ZengXiangRui.Common.exception.util.RequestParametersException;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("all")
public class BookKeepingServiceImpl extends ServiceImpl<BookKeepingBillMapper, BookKeepingBill>
        implements BookKeepingService {

    @Autowired
    BookKeepingBillMapper bookKeepingBillMapper;

    @Override
    @DS("from")
    @LoggerAnnotation(operation = "查询账单信息", dataSource = "from")
    public String getBookKeeping(int page, int quantity) throws DataBaseException, RequestParametersException {
        if (page < 1 || quantity < 1) {
            throw new RequestParametersException("参数规格不正确");
        }
        Page<BookKeepingBill> bookKeepingBills;
        try {
            bookKeepingBills = bookKeepingBillMapper.selectPage(
                    new Page<>(page, quantity),
                    new LambdaQueryWrapper<BookKeepingBill>()
            );
        } catch (Exception exception) {
            throw new DataBaseException(exception.getMessage());
        }
        return new JsonSerialization().toJson(new BookKeepingBillsResponse<Page<BookKeepingBill>>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, bookKeepingBills
        ));
    }

    @Override
    @DS("master")
    @DSTransactional
    @LoggerAnnotation(operation = "创建账单", dataSource = "master")
    public String createBookKeeping(BookKeepingBill bookKeepingDisplayBill) throws DataBaseException, RequestParametersException {
        if (bookKeepingDisplayBill.getTradeOrderNumber() == null) {
            throw new RequestParametersException("参数规格不正确");
        }
        try {
            String id = Encryption.encryptToMd5(bookKeepingDisplayBill.getTradeOrderNumber());
            BookKeepingBill bookKeepingBill = new BookKeepingBill();
            bookKeepingBill.setId(id);
            bookKeepingBill.setTradeOrderNumber(bookKeepingDisplayBill.getTradeOrderNumber());
            bookKeepingBill.setCounterparty(bookKeepingDisplayBill.getCounterparty());
            bookKeepingBill.setRemarks(bookKeepingDisplayBill.getRemarks());
            bookKeepingBill.setTradingHours(bookKeepingDisplayBill.getTradingHours());
            bookKeepingBill.setTransactionClassification(bookKeepingDisplayBill.getTransactionClassification());
            bookKeepingBill.setAmountOfTransaction(bookKeepingDisplayBill.getAmountOfTransaction());
            bookKeepingBill.setTransactionClassification(bookKeepingDisplayBill.getTransactionClassification());
            bookKeepingBill.setModeOfTransaction(bookKeepingDisplayBill.getModeOfTransaction());
            bookKeepingBill.setCounterparty(bookKeepingDisplayBill.getCounterparty());
            bookKeepingBill.setProductDescription(bookKeepingDisplayBill.getProductDescription());
            bookKeepingBill.setTransactionStatus(bookKeepingDisplayBill.getTransactionStatus());
            bookKeepingBillMapper.insert(bookKeepingBill);
        } catch (Exception exception) {
            throw new DataBaseException(exception.getMessage());
        }
        return JsonSerialization.toJson(new BookKeepingBillsResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "添加成功"
        ));
    }

    @Override
    @DS("master")
    @DSTransactional
    @LoggerAnnotation(operation = "修改账单", dataSource = "master")
    public String updateBookKeeping(BookKeepingBill bookKeepingDisplayBill) throws DataBaseException {
        if (bookKeepingDisplayBill.getId() == null || bookKeepingDisplayBill.getTradeOrderNumber() == null) {
            throw new RequestParametersException("参数规格不正确");
        }
        try {
            bookKeepingBillMapper.update(bookKeepingDisplayBill,
                    new LambdaQueryWrapper<BookKeepingBill>().eq(BookKeepingBill::getId, bookKeepingDisplayBill.getId()));
        } catch (Exception exception) {
            throw new DataBaseException(exception.getMessage());
        }
        return JsonSerialization.toJson(new BookKeepingBillsResponse<String>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "修改成功"
        ));
    }
}
