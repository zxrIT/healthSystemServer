package com.ZengXiangRui.Common.exception;

import com.ZengXiangRui.Common.Response.BaseResponse;
import com.ZengXiangRui.Common.Response.BaseResponseUtil;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.exception.util.ForbiddenException;
import com.ZengXiangRui.Common.exception.util.RequestParametersException;
import com.ZengXiangRui.Common.exception.util.UploadFailedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String exceptionHandler(Exception exception) {
        if (exception instanceof ForbiddenException) {
            return JsonSerialization.toJson(new BaseResponse<>(
                    BaseResponseUtil.FORBIDDEN_CODE, BaseResponseUtil.FORBIDDEN_MESSAGE,
                    "认证失败，请重新登录"
            ));
        } else if (exception instanceof RequestParametersException) {
            return JsonSerialization.toJson(new BaseResponse<>(
                    BaseResponseUtil.CLIENT_ERROR_CODE, BaseResponseUtil.CLIENT_ERROR_MESSAGE,
                    exception.getMessage()
            ));
        } else if (exception instanceof UploadFailedException) {
            return JsonSerialization.toJson(new BaseResponse<>(
                    BaseResponseUtil.CLIENT_ERROR_CODE, BaseResponseUtil.CLIENT_ERROR_MESSAGE,
                    "上传失败请稍后重试"
            ));
        }
        logger.error("==============log start =============");
        logger.error("exception type: {}", exception.getClass().getName());
        logger.error("exception message: {}", exception.getMessage());
        logger.error("==============log end =============");
        return JsonSerialization.toJson(new BaseResponse<>(
                BaseResponseUtil.SERVER_ERROR_CODE, BaseResponseUtil.SERVER_ERROR_MESSAGE,
                "服务器去了火星！！！请稍后重试"
        ));
    }
}
