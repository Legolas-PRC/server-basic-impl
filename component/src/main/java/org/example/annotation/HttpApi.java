package org.example.annotation;

import org.example.enums.HttpMethod;

import java.lang.annotation.*;

/**
 * @author chenqian
 * @date 2024/11/14 11:27
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface HttpApi {
}
