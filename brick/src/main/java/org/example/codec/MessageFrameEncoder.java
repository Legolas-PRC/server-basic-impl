package org.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.example.codec.compress.Compressor;
import org.example.codec.compress.GzipCompressor;
import org.example.codec.serialize.ProtostuffSerializer;
import org.example.codec.serialize.Serializer;
import org.example.common.CompressSupport;
import org.example.common.Constants;
import org.example.common.SerializeSupport;
import org.example.protocol.MessageFrame;
import org.example.utils.GsonUtil;
import org.example.utils.SingletonFactory;

/**
 * 消息编码
 * @author chenqian
 * @date 2024/12/23 19:51
 **/
@Slf4j
public class MessageFrameEncoder extends MessageToByteEncoder<MessageFrame> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageFrame messageFrame, ByteBuf byteBuf) {
        // 需要单例实现
        Serializer serializer = SingletonFactory.getInstance(ProtostuffSerializer.class);
        Compressor compressor = SingletonFactory.getInstance(GzipCompressor.class);

        log.info("encode messageFrame={}", GsonUtil.toJson(messageFrame));
        byte[] data = compressor.compress(serializer.serialize(messageFrame.getData()));
        int length = data.length;
        int messageLength = Constants.HEAD_LENGTH + length;

        // 消息长度
        byteBuf.writeInt(messageLength);
        // 请求id
        byteBuf.writeInt(messageFrame.getRequestId());
        // 消息类型
        byteBuf.writeByte(messageFrame.getMessageType());
        // 序列化类型
        byteBuf.writeByte(SerializeSupport.PROTOSTUFF.getCode());
        // 压缩类型
        byteBuf.writeByte(CompressSupport.GZIP.getCode());
        // 数据
        byteBuf.writeBytes(data);
    }
}
