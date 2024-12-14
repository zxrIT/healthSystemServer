package com.ZengXiangRui.Common.Utils;

import com.ZengXiangRui.Common.exception.util.JsonSerializationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtil() {
    }

    public static String objectToJson(Object object) throws JsonSerializationException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException jsonProcessingException) {
            throw new JsonSerializationException(jsonProcessingException.getMessage());
        }
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) throws JsonSerializationException {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException jsonProcessingException) {
            throw new JsonSerializationException(jsonProcessingException.getMessage());
        }
    }
}
