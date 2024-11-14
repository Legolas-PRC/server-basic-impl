package org.example;

import org.example.annotation.HttpApi;
import org.example.annotation.HttpMapping;
import org.example.enums.HttpMethod;
import org.reflections.Reflections;
import sun.reflect.Reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenqian
 * @date 2024/11/13 19:46
 **/
public class Handler {
    private static Map<String, Handler> handlerMap = new HashMap<>();

    public static void baseHandle(HttpRequest httpRequest, HttpResponse httpResponse) {
        String path = httpRequest.getPath();
        Handler handler = handlerMap.get(path);
        if (handler == null) {
            handleMiss(httpRequest, httpResponse);
        } else {
            switch (httpRequest.getMethod()) {
                case GET:
                    handler.handleGet(httpRequest, httpResponse);
                    break;
                case POST:
                    handler.handlePost(httpRequest, httpResponse);
                    break;
                case PUT:
                    handler.handlePut(httpRequest, httpResponse);
                    break;
                case DELETE:
                    handler.handleDelete(httpRequest, httpResponse);
                    break;
                default:
                    handleMiss(httpRequest, httpResponse);
                    break;
            }
        }

    }


    public void handleGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        handle(httpRequest,httpResponse);
    }

    public void handlePost(HttpRequest httpRequest, HttpResponse httpResponse) {

    }

    public void handlePut(HttpRequest httpRequest, HttpResponse httpResponse) {

    }

    public void handleDelete(HttpRequest httpRequest, HttpResponse httpResponse) {

    }

    public static void handleMiss(HttpRequest httpRequest, HttpResponse httpResponse) {
        System.out.println("没有对应handler");
    }

    public void handle(Object request,Object response) {

    }

    private static void registerHandle(HttpMapping annotation,Method method,Object instance) {
        Handler handler = new Handler(){
            @Override
            public void handle(Object request, Object response) {
                try {
                    method.invoke(instance, request, response);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

        handlerMap.put(annotation.path(), handler);
    }

    public static void registerHandlers(List<String> packages) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections(packages);
        Set<Class<?>> clazzSet = reflections.getTypesAnnotatedWith(HttpApi.class);
        for (Class<?> clazz : clazzSet) {
            Method[] methods = clazz.getMethods();
            Object instance = clazz.getDeclaredConstructor().newInstance();
            for (Method method : methods) {
                if(method.isAnnotationPresent(HttpMapping.class)){
                    registerHandle(method.getAnnotation(HttpMapping.class), method, instance);
                }
            }
        }
    }

}
