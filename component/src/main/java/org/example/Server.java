package org.example;

import org.example.Http.HttpParser;
import org.example.Http.HttpRequest;
import org.example.Http.HttpResponse;
import org.example.enums.HttpMethod;
import org.example.enums.HttpVersion;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenqian
 * @date 2024/11/13 17:37
 **/
public interface Server {
    void start();

    /**
     * 消息体
     * @param socket
     * @throws IOException
     */
    default void handleClient(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter writer = new PrintWriter(outputStream, true);
        HttpRequest httpRequest = HttpParser.parseHttpRequest(reader);
        System.out.println(httpRequest);
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setHttpVersion(httpRequest.getVersion());
        httpResponse.setStatusCode(200);
        httpResponse.setStatusMessage("OK");
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        httpResponse.setHeaders(headers);
        Handler.baseHandle(httpRequest, httpResponse);
        System.out.println(httpResponse.toResponseMessage());
        writer.println(httpResponse.toResponseMessage());
    }


}
