package com.ruoyi.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JsonUtils {
    private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // JSON转指定类型对象
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        if (isEmpty(jsonStr)) {
            log.warn("JSON字符串为空，无法解析为{}类型", clazz.getName());
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStr, clazz);
        } catch (Exception e) {
            log.error("Jackson解析JSON失败！", e);
            return null;
        }
    }

    // JSON转复杂类型（如Map<String, Boolean>）
    public static <T> T parseObject(String jsonStr, TypeReference<T> typeReference) {
        if (isEmpty(jsonStr)) {
            log.warn("JSON字符串为空，无法解析为{}类型", typeReference.getType().getTypeName());
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStr, typeReference);
        } catch (Exception e) {
            log.error("Jackson解析复杂类型JSON失败！", e);
            return null;
        }
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
