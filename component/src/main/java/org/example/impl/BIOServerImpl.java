package org.example.impl;

import org.example.Handler;
import org.example.Http.HttpRequest;
import org.example.Http.HttpResponse;
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
 * 基于BIO 同步阻塞实现
 * 一个客户端连接一个处理线程
 * accept方法阻塞等待连接，read方法阻塞等待数据
 *
 * @author chenqian
 * @date 2024/11/13 17:41
 **/
public class BIOServerImpl implements Server {
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
                        InputStream inputStream = socket.getInputStream();
                        OutputStream outputStream = socket.getOutputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        PrintWriter writer = new PrintWriter(outputStream, true);

                        HttpRequest httpRequest = new HttpRequest();
                        // 解析请求行
                        // 阻塞线程，等待数据
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

                        HttpResponse httpResponse = new HttpResponse();

                        Handler.baseHandle(httpRequest, httpResponse);
                        
                        writer.println(httpResponse.toResponseMessage());
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




}
