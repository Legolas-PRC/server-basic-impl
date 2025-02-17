package org.example.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenqian
 * @date 2024/12/26 16:46
 **/
public class SingletonFactory {
    private static final Map<Class<?>, Object> INSTANCE_MAP = new ConcurrentHashMap<>();

    public static <T> T getInstance(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class is null");
        }
        if (INSTANCE_MAP.containsKey(clazz)) {
            return clazz.cast(INSTANCE_MAP.get(clazz));
        } else {
            return clazz.cast(INSTANCE_MAP.computeIfAbsent(clazz, key -> {
                try {
                    return key.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
        }
    }
}
