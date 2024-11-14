package org.example;

import org.example.enums.HttpMethod;
import org.example.enums.HttpVersion;

import java.util.Map;

/**
 * @author chenqian
 * @date 2024/11/13 19:19
 **/
public class HttpRequest {
    private HttpMethod method;
    private String path;
    private HttpVersion version;
    private Map<String, String> headers;
    private String body;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpVersion getVersion() {
        return version;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", version=" + version +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }
}
