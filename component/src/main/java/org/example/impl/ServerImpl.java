package org.example.impl;

import org.example.Handler;
import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.Server;
import org.example.enums.HttpMethod;
import org.example.enums.HttpVersion;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenqian
 * @date 2024/11/13 17:41
 **/
public class ServerImpl implements Server {
    @Override
    public void start() {
        System.out.println("Server started.");
        try {
            Handler.registerHandlers(Collections.singletonList("org.example.controller"));
            ServerSocket serverSocket = new ServerSocket(10010);
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
            while (true) {
                // 阻塞，等待客户端连接
                Socket socket = serverSocket.accept();
                // 将连接丢到线程池里，主线程继续等待连接
                threadPool.execute(() -> {
                    try {
                        handleClient(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {

        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 消息体
     * @param socket
     * @throws IOException
     */
    public void handleClient(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        PrintWriter writer = new PrintWriter(outputStream, true);
        HttpRequest httpRequest = parseHttpRequest(reader);
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

    // 解析http请求
    public HttpRequest parseHttpRequest(BufferedReader reader) throws IOException {
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
