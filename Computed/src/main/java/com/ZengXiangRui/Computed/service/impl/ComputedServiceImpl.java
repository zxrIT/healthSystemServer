package com.ZengXiangRui.Computed.service.impl;

import com.ZengXiangRui.Common.Entity.Bill;
import com.ZengXiangRui.Common.Entity.User;
import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.Utils.UserContext;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Computed.entity.BillEntity;
import com.ZengXiangRui.Computed.entity.MoneyEntity;
import com.ZengXiangRui.Computed.entity.UploadTimeEntity;
import com.ZengXiangRui.Computed.mapper.BillMapper;
import com.ZengXiangRui.Computed.mapper.MoneyMapper;
import com.ZengXiangRui.Computed.mapper.UploadTimeMapper;
import com.ZengXiangRui.Computed.mapper.UserMapper;
import com.ZengXiangRui.Computed.response.ComputedResponse;
import com.ZengXiangRui.Computed.service.ComputedService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@SuppressWarnings("all")
@RequiredArgsConstructor
public class ComputedServiceImpl extends
        ServiceImpl<BillMapper, BillEntity> implements ComputedService {

    @Autowired
    private final BillMapper billMapper;

    @Autowired
    private final UploadTimeMapper uploadTimeMapper;

    @Autowired
    private final MoneyMapper moneyMapper;

    @Autowired
    private final UserMapper userMapper;

    @Override
    @DSTransactional
    @LoggerAnnotation(operation = "计算用户的全部支出保存数据库",
            dataSource = "mysql集群从库和本地money表")
    public void setTotalSpending(String userId) {
        try {
            System.err.println(userId);
            MoneyEntity moneyEntity = new MoneyEntity();
            DynamicDataSourceContextHolder.push("from");
            Integer incomeMonth = billMapper.calculateBillMonthsSpan(userId);
            Double spending = billMapper.selectTotalAmountByUserId(userId);
            DynamicDataSourceContextHolder.push("master");
            User user = userMapper.selectOne(
                    new LambdaQueryWrapper<User>().eq(User::getId, userId));
            moneyEntity.setUserId(userId);
            moneyEntity.setDisburse(spending);
            moneyEntity.setIncome(user.getSalary() * incomeMonth);
            moneyEntity.setSurplus(user.getSalary() * incomeMonth - spending);
            if (moneyMapper.exists(new LambdaQueryWrapper<MoneyEntity>().eq(MoneyEntity::getUserId, userId))) {
                moneyMapper.update(moneyEntity,
                        new LambdaUpdateWrapper<MoneyEntity>().eq(MoneyEntity::getUserId, userId));
                return;
            }
            moneyMapper.insert(moneyEntity);
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
    }

    @Override
    @DS("master")
    @DSTransactional
    @LoggerAnnotation(operation = "计算提交时间", dataSource = "本地的表uploadTime表")
    public void setUploadTime(String userId, String type, Date uploadTime) {
        System.out.println(userId);
        UploadTimeEntity uploadTimeEntity = new UploadTimeEntity();
        uploadTimeEntity.setUserId(userId);
        boolean exists = uploadTimeMapper.exists(new LambdaQueryWrapper<UploadTimeEntity>().eq(UploadTimeEntity::getUserId, userId));
        switch (type) {
            case "ali":
                uploadTimeEntity.setAlipay(uploadTime);
                if (exists) {
                    uploadTimeMapper.update(uploadTimeEntity,
                            new LambdaUpdateWrapper<UploadTimeEntity>().eq(UploadTimeEntity::getUserId, userId));
                    return;
                }
                uploadTimeMapper.insert(uploadTimeEntity);
                return;
            case "wechat":
                uploadTimeEntity.setWechat(uploadTime);
                if (exists) {
                    uploadTimeMapper.update(uploadTimeEntity,
                            new LambdaUpdateWrapper<UploadTimeEntity>().eq(UploadTimeEntity::getUserId, userId));
                    return;
                }
                uploadTimeMapper.insert(uploadTimeEntity);
        }
    }

    @Override
    @DS("master")
    @LoggerAnnotation(operation = "查询用户的消费情况", dataSource = "本地的money表")
    public String getTotalSpending() {
        MoneyEntity moneyEntity = moneyMapper.selectOne(new LambdaQueryWrapper<MoneyEntity>()
                .eq(MoneyEntity::getUserId, UserContext.getUserId()));
        return JsonSerialization.toJson(new ComputedResponse<MoneyEntity>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, moneyEntity
        ));
    }

    @Override
    @DS("master")
    @LoggerAnnotation(operation = "查询用户的消费情况", dataSource = "本地的money表")
    public String getUploadTime() {
        UploadTimeEntity uploadTimeEntity = uploadTimeMapper.selectOne(new LambdaQueryWrapper<UploadTimeEntity>()
                .eq(UploadTimeEntity::getUserId, UserContext.getUserId()));
        return JsonSerialization.toJson(new ComputedResponse<UploadTimeEntity>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, uploadTimeEntity
        ));
    }
}
