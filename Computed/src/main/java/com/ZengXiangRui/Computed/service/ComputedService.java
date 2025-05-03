package com.ZengXiangRui.Computed.service;

import com.ZengXiangRui.Computed.entity.BillEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

public interface ComputedService extends IService<BillEntity> {
    void setTotalSpending(String userId);

    void setUploadTime(String userId, String type, Date uploadTime);

    String getTotalSpending();

    String getUploadTime();
}
