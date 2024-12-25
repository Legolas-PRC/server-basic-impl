package org.example;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用请求类型
 * @author chenqian
 * @date 2024/12/23 19:45
 **/
@Data
public class Request implements Serializable {
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] parameterTypes;
}
