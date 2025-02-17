package org.example.codec.serialize;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @author chenqian
 * @date 2024/12/25 19:55
 **/
public class ProtostuffSerializer implements Serializer {

    public static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);

    @Override
    public byte[] serialize(Object object) {
        byte[] bytes;
        try {
            Schema schema = RuntimeSchema.getSchema(object.getClass());
            bytes = ProtostuffIOUtil.toByteArray(object, schema, BUFFER);
        } finally  {
            BUFFER.clear();
        }
        return bytes;
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T object = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, object, schema);
        return object;
    }
}
