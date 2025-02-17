package org.example.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息帧 11byte
 * 消息长度：4byte
 * 请求ID：4byte
 * 消息类型：1byte
 * 序列化类型：1byte
 * 压缩类型：1byte
 * @author chenqian
 * @date 2024/12/23 19:34
 **/
@Data
public class MessageFrame implements Serializable {
    private int requestId;
    private byte messageType;
    private byte serializeType;
    private byte compressType;
    private Object data;
}
