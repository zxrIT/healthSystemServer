package com.ZengXiangRui.Computed.service;

import com.ZengXiangRui.Computed.entity.BillEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Date;

public interface ComputedService extends IService<BillEntity> {
    String sendMessage();

    void setTotalSpending(String userId);

    void setUploadTime(String userId, String type, Date uploadTime);

    String getUser();

    String getTotalSpending();

    String getUploadTime();

    String serUserThreshold(int threshold);

    String setUserSalary(double salary);

    String getDateDayData(Date startDate, Date endDate);
}
