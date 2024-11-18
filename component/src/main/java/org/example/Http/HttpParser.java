package org.example.Http;

import org.example.enums.HttpMethod;
import org.example.enums.HttpVersion;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenqian
 * @date 2024/11/18 16:41
 **/
public class HttpParser {
    // 解析http请求
    public static HttpRequest parseHttpRequest(BufferedReader reader) throws IOException {
        /**
         * POST /test HTTP/1.1
         * Host: 127.0.0.1:10010
         * User-Agent: curl/7.64.1
         *
         * body
         */

        HttpRequest httpRequest = new HttpRequest();
        // 解析请求行
        String requestLine = reader.readLine();
        while (requestLine == null) {
            requestLine = reader.readLine();
        }
        String[] requestParts = requestLine.split(" ");
        httpRequest.setMethod(HttpMethod.getHttpMethod(requestParts[0]));
        httpRequest.setPath(requestParts[1]);
        httpRequest.setVersion(HttpVersion.getHttpVersion(requestParts[2]));

        // 解析请求头
        Map<String, String> headerMap = new HashMap<>();
        String headerLine;
        while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
            String[] headerParts = headerLine.split(": ", 2);
            headerMap.put(headerParts[0], headerParts[1]);
        }
        httpRequest.setHeaders(headerMap);

        // 解析请求体
        StringBuilder body = new StringBuilder();
        while (reader.ready()) {
            body.append((char) reader.read());
        }
        httpRequest.setBody(body.toString());

        return httpRequest;
    }
}
