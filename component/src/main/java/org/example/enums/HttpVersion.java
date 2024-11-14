package org.example.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenqian
 * @date 2024/11/13 19:26
 **/
public enum HttpVersion {
    HTTP_1_0("HTTP/1.0"),
    HTTP_1_1("HTTP/1.1"),
    HTTP_2_0("HTTP/2.0");

    private String version;

    HttpVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    private static Map<String,HttpVersion> versionMap = new HashMap<>();

    static {
        for (HttpVersion version : HttpVersion.values()) {
            versionMap.put(version.getVersion(), version);
        }
    }

    public static HttpVersion getHttpVersion(String version) {
        return versionMap.get(version);
    }

}
