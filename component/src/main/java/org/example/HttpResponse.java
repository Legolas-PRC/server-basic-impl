package org.example;

import org.example.enums.HttpVersion;

import java.util.Map;

/**
 * @author chenqian
 * @date 2024/11/13 19:40
 **/
public class HttpResponse {
    private HttpVersion httpVersion;
    private int statusCode;
    private String statusMessage;
    private Map<String, String> headers;
    private String body;

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(HttpVersion httpVersion) {
        this.httpVersion = httpVersion;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
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

    public String toResponseMessage() {
        StringBuilder response = new StringBuilder();
        response.append(httpVersion.getVersion()).append(" ").append(statusCode).append(" ").append(statusMessage).append("\r\n");

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            response.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }

        response.append("\r\n");

        if (body != null) {
            response.append(body);
        }

        return response.toString();
    }
}
