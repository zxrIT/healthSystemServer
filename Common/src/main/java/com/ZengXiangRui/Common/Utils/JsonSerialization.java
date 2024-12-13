package com.ZengXiangRui.Common.Utils;

import com.google.gson.Gson;

public class JsonSerialization {
    private final static Gson gson = new Gson();

    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
