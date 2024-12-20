package com.ZengXiangRui.upload.service.impl;

import com.ZengXiangRui.Common.Entity.AMQP.BatchCreateAMQPResult;
import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.ErrorLogger;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import com.ZengXiangRui.Common.exception.util.UploadFailedException;
import com.ZengXiangRui.upload.aliyun.AliyunOSSProvider;
import com.ZengXiangRui.upload.entity.response.UploadResponseData;
import com.ZengXiangRui.upload.entity.response.UploadResponse;
import com.ZengXiangRui.upload.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@Slf4j
@SuppressWarnings("all")
public class UploadServiceImpl implements UploadService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private AliyunOSSProvider aliyunOSSProvider;

    @Override
    @LoggerAnnotation(operation = "上传本地csv", dataSource = "")
    public String uploadCsv(MultipartFile uploadFile, String username, String userid, String notesOnBills) throws UploadFailedException {
        UUID randomUUIDString = UUID.randomUUID();
        String fileName = username + "_" + userid + "_" + randomUUIDString + ".csv";
        try {
            uploadFile.transferTo(new File("/Users/zengxiangrui/HealthSystem/static/csv/" + fileName));
            aliyunOSSProvider.aliyunUpdate(fileName);
            BatchCreateAMQPResult<String> batchCreateAMQPResult = new BatchCreateAMQPResult<>();
            batchCreateAMQPResult.setId(randomUUIDString.toString());
            batchCreateAMQPResult.setData("/Users/zengxiangrui/HealthSystem/static/csv/" + fileName);
            rabbitTemplate.convertAndSend("zxr.HealthExchange.ali.csv", "", batchCreateAMQPResult);
        } catch (Exception exception) {
            File file = new File("/Users/zengxiangrui/HealthSystem/static/csv/" + fileName);
            if (file.exists()) {
                file.delete();
            }
            try {
                aliyunOSSProvider.aliyunDelete(fileName);
            } catch (Exception exceptionAliyun) {
                ErrorLogger.Log(UploadFailedException.class, exceptionAliyun.getMessage());
            } finally {
                throw new UploadFailedException(exception.getMessage());
            }
        }
        return JsonSerialization.toJson(new UploadResponse<UploadResponseData>(
                BaseResponseUtil.SUCCESS_CODE, BaseResponseUtil.SUCCESS_MESSAGE,
                new UploadResponseData(randomUUIDString.toString(), "数据处理中......")
        ));
    }
}
