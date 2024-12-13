package com.ZengXiangRui.Common.Utils;

import java.lang.reflect.Parameter;

public class IsEmpty {
    public static boolean isParametersArrayEmpty(Parameter[] parameters) {
        return parameters == null || parameters.length == 0;
    }
}
