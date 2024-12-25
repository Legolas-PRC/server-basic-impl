package org.example;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

/**
 * 消息编码
 * @author chenqian
 * @date 2024/12/23 19:51
 **/
public class MessageFrameEncoder extends MessageToByteEncoder<MessageFrame> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageFrame messageFrame, ByteBuf byteBuf) throws Exception {
        // 需要单例实现

    }
}
