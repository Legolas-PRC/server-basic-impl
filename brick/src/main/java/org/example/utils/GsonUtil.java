package org.example.utils;

import com.google.gson.Gson;

/**
 * @author chenqian
 * @date 2024/12/26 17:32
 **/
public class GsonUtil {
    private static final Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
