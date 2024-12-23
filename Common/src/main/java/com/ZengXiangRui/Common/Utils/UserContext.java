package com.ZengXiangRui.Common.Utils;

public class UserContext {
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setUserId(String userId) {
        threadLocal.set(userId);
    }

    public static String getUserId() {
        return threadLocal.get();
    }

    public static void removeUserId() {
        threadLocal.remove();
    }
}
