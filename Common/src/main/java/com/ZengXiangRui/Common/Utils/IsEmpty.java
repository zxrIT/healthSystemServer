package com.ZengXiangRui.Common.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Parameter;

public class IsEmpty {
    public static boolean isParametersArrayEmpty(Parameter[] parameters) {
        return parameters == null || parameters.length == 0;
    }

    public static boolean isJsonEmpty(String jsonString) {
        JSONObject jsonObject = JSON.parseObject(jsonString);
        return jsonObject == null || jsonObject.isEmpty();
    }

    public static boolean isStringEmpty(String string) {
        return string == null || string.isEmpty();
    }
}
