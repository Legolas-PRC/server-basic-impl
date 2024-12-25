package org.example;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用响应类型
 * @author chenqian
 * @date 2024/12/23 19:47
 **/
@Data
public class Response<T> implements Serializable {
    private String requestId;
    private Integer code;
    private String message;
    private T data;
}
