package com.ZengXiangRui.Common.aop;

import com.ZengXiangRui.Common.Utils.IsEmpty;
import com.ZengXiangRui.Common.Utils.JsonSerialization;
import com.ZengXiangRui.Common.annotation.LoggerAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
@Slf4j
public class LoggerAspect {

    @Pointcut("@annotation(com.ZengXiangRui.Common.annotation.LoggerAnnotation)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object aroundLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - beginTime;
        recordLogger(joinPoint, executionTime);
        return result;
    }

    private void recordLogger(ProceedingJoinPoint joinPoint, long executionTime) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LoggerAnnotation loggerAnnotation = method.getAnnotation(LoggerAnnotation.class);
        log.info("==============log start =============");
        log.info("operation:{}", loggerAnnotation.operation());
        log.info("dataSource:{}", loggerAnnotation.dataSource());
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        log.info("class:{}, method:{}", className, methodName);
        log.info("executionTime:{}", executionTime);
        log.info("==============log end =============");
    }
}
