package org.example.common;

/**
 * @author chenqian
 * @date 2024/12/26 17:40
 **/
public interface Constants {
    /**
     * 消息帧 11byte
     * 消息长度：4byte
     * 请求ID：4byte
     * 消息类型：1byte
     * 序列化类型：1byte
     * 压缩类型：1byte
     **/
    int HEAD_LENGTH = 11;

    byte MESSAGE_TYPE_REQUEST = 0;
    byte MESSAGE_TYPE_RESPONSE = 1;
}
