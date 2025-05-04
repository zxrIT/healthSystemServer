package com.ZengXiangRui.Computed.service.impl;

import com.ZengXiangRui.Common.Entity.User;
import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.Utils.UserContext;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Computed.entity.BillEntity;
import com.ZengXiangRui.Computed.entity.DailyConsumptionDTO;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@SuppressWarnings("all")
@RequiredArgsConstructor
public class ComputedServiceImpl extends
        ServiceImpl<BillMapper, BillEntity> implements ComputedService {
    private final ThreadLocal<String> emailTitle = ThreadLocal.withInitial(() -> "花费预警");
    private final ThreadLocal<String> emailServerAddress = ThreadLocal.withInitial(() -> "healthSystem@126.com");

    @Autowired
    private final BillMapper billMapper;

    @Autowired
    private final UploadTimeMapper uploadTimeMapper;

    @Autowired
    private final MoneyMapper moneyMapper;

    @Autowired
    private final JavaMailSender javaMailSender;

    @Autowired
    private final UserMapper userMapper;

    private static final Logger log = LoggerFactory.getLogger(ComputedServiceImpl.class);

    @Override
    @DSTransactional
    public String sendMessage() {
        try {
            DynamicDataSourceContextHolder.push("from");
            BillEntity billEntitystart = billMapper.selectOne(new LambdaQueryWrapper<BillEntity>()
                    .orderByAsc(BillEntity::getTradingHours).last("limit 1"));
            BillEntity billEntityend = billMapper.selectOne(new LambdaQueryWrapper<BillEntity>()
                    .orderByDesc(BillEntity::getTradingHours).last("limit 1"));
            LocalDateTime oldestDate = billEntitystart.getTradingHours().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime newestDate = billEntityend.getTradingHours().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            Period period = Period.between(
                    oldestDate.toLocalDate(),
                    newestDate.toLocalDate());
            int monthsDiff = period.getYears() * 12 + period.getMonths();
            AtomicReference<Double> userCount = new AtomicReference<>((double) 0);
            List<BillEntity> billEntities = billMapper.selectList(new LambdaQueryWrapper<BillEntity>().eq(
                            BillEntity::getUserId, UserContext.getUserId())
                    .eq(BillEntity::getDirectionOfTrade, 1));
            billEntities.stream().forEach(billEntity -> {
                userCount.updateAndGet(v -> new Double((double) (v + billEntity.getAmountOfTransaction())));
            });
            DynamicDataSourceContextHolder.push("master");
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getId, UserContext.getUserId()));
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(String.format("%d个月您一共消费了:%.2f", monthsDiff, userCount.get()));
            stringBuffer.append(String.format("%d个月您一共获得了:%.2f", monthsDiff, user.getSalary() * monthsDiff));
            stringBuffer.append(String.format("您的阈值为%d%%即不超过:%.2f¥", user.getThreshold(),
                    ((1 + user.getThreshold()) * 0.01) * user.getSalary() * monthsDiff));
            if (((((1 + user.getThreshold()) * 0.01) * user.getSalary() * monthsDiff) - userCount.get()) > 0) {
                stringBuffer.append(String.format("所以您还没有超出您的预算"));
            } else {
                stringBuffer.append(String.format("所以您超出您的预算"));
            }
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject(emailTitle.get());
            simpleMailMessage.setText(stringBuffer.toString());
            simpleMailMessage.setTo(user.getEmail());
            simpleMailMessage.setFrom(emailServerAddress.get());
            javaMailSender.send(simpleMailMessage);
            return JsonSerialization.toJson(new ComputedResponse<String>(
                    BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, "success"
            ));
        } finally {
            DynamicDataSourceContextHolder.clear();
        }
    }

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
    public String getUser() {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getId, UserContext.getUserId()));
        return JsonSerialization.toJson(new ComputedResponse<User>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, user
        ));
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

    @Override
    @DS("master")
    @DSTransactional
    @LoggerAnnotation(operation = "修改用户的阈值", dataSource = "本地的user表")
    public String serUserThreshold(int threshold) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getId, UserContext.getUserId()));
        user.setThreshold(threshold);
        userMapper.update(user, new LambdaUpdateWrapper<User>().eq(
                User::getId, UserContext.getUserId()));
        return JsonSerialization.toJson(new ComputedResponse<User>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, user
        ));
    }

    @Override
    @DS("master")
    @DSTransactional
    @LoggerAnnotation(operation = "修改用户的工资", dataSource = "本地的user表")
    public String setUserSalary(double salary) {
        User user = userMapper.selectOne
                (new LambdaQueryWrapper<User>().eq(User::getId, UserContext.getUserId()));
        user.setSalary(salary);
        userMapper.update(user, new LambdaUpdateWrapper<User>().eq(User::getId, UserContext.getUserId()));
        return JsonSerialization.toJson(new ComputedResponse<User>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE, user
        ));
    }

    @Override
    @DS("from")
    @LoggerAnnotation(operation = "查询用户每日消费数据", dataSource = "mysql集群从库")
    public String getDateDayData(Date startDate, Date endDate) {
        try {
            String userId = UserContext.getUserId();
            List<DailyConsumptionDTO> dailyData = billMapper.selectDailyConsumptionByDateRange(
                    userId, startDate, endDate);
            return JsonSerialization.toJson(new ComputedResponse<>(
                    BaseResponseUtil.SUCCESS_CODE,
                    BaseResponseUtil.SUCCESS_MESSAGE,
                    dailyData
            ));
        } catch (Exception e) {
            log.error("获取日期消费数据失败", e);
            return JsonSerialization.toJson(new ComputedResponse<>(
                    BaseResponseUtil.SUCCESS_CODE,
                    "获取日期消费数据失败",
                    null
            ));
        }
    }
}
