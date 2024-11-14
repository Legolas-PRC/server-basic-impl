package org.example.controller;

import org.example.HttpRequest;
import org.example.HttpResponse;
import org.example.annotation.HttpApi;
import org.example.annotation.HttpMapping;
import org.example.enums.HttpMethod;

/**
 * @author chenqian
 * @date 2024/11/14 15:58
 **/
@HttpApi
public class AddController {
    @HttpMapping(path = "/add", method = HttpMethod.GET)
    public void add(HttpRequest request,HttpResponse response) {
        response.setBody("add success");
        System.out.println(response);
    }
}
