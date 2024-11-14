package org.example.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenqian
 * @date 2024/11/13 19:21
 **/
public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    private static Map<String, HttpMethod> methodMap = new HashMap<>();

    static {
        for (HttpMethod httpMethod : HttpMethod.values()) {
            methodMap.put(httpMethod.getMethod(), httpMethod);
        }
    }

    public static HttpMethod getHttpMethod(String method) {
        return methodMap.get(method);
    }
}
