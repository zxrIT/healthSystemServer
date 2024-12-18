package com.ZengXiangRui.Common.Utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ErrorLogger {
    private static final Logger log = LogManager.getLogger(ErrorLogger.class);

    public static void Log(Class<?> clazz, String message) {
        log.error("==============error start==============");
        log.error("====error class{}========", clazz.getName());
        log.error("====error message{}========", message);
        log.error("==============error end==============");
    }
}
