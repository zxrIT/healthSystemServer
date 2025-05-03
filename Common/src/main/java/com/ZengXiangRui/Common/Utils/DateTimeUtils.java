package com.ZengXiangRui.Common.Utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

public class DateTimeUtils {

    private static final ZoneId SHANGHAI_ZONE = ZoneId.of("Asia/Shanghai");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        // 设置SimpleDateFormat的时区为上海
        DATE_FORMAT.setTimeZone(java.util.TimeZone.getTimeZone(SHANGHAI_ZONE));
    }

    /**
     * 获取上海当前时间
     * @return Date对象，上海当前时间
     */
    public static Date getCurrentDateTime() {
        return Date.from(LocalDateTime.now(SHANGHAI_ZONE).atZone(SHANGHAI_ZONE).toInstant());
    }

    /**
     * 获取指定时间的零点时间（上海时区）
     * @param date 指定的时间
     * @return Date对象，零点时间
     */
    public static Date getDayStart(Date date) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(SHANGHAI_ZONE)
                .toLocalDateTime()
                .with(LocalTime.MIN);
        return Date.from(localDateTime.atZone(SHANGHAI_ZONE).toInstant());
    }

    /**
     * 获取指定时间的24点时间（上海时区）
     * @param date 指定的时间
     * @return Date对象，24点时间
     */
    public static Date getDayEnd(Date date) {
        LocalDateTime localDateTime = date.toInstant()
                .atZone(SHANGHAI_ZONE)
                .toLocalDateTime()
                .with(LocalTime.MAX);
        return Date.from(localDateTime.atZone(SHANGHAI_ZONE).toInstant());
    }

    /**
     * 格式化日期为字符串（上海时区）
     * @param date 日期
     * @return 格式化后的字符串 (yyyy-MM-dd HH:mm:ss)
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }
}