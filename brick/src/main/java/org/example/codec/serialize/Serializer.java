package org.example.codec.serialize;

/**
 * @author chenqian
 * @date 2024/12/25 19:55
 **/
public interface Serializer {
    byte[] serialize(Object object);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
