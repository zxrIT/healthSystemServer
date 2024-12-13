package com.ZengXiangRui.BookKeepingProvider.entity;

import com.ZengXiangRui.Common.Entity.Bill;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bookKeepingBill")
public class BookKeepingBill extends Bill {
}
