package org.example.annotation;

import org.example.enums.HttpMethod;

import java.lang.annotation.*;

/**
 * @author chenqian
 * @date 2024/11/14 15:40
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface HttpMapping {
    String path();
    HttpMethod method();
}
