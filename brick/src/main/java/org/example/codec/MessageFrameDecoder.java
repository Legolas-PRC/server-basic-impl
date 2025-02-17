package org.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.example.codec.compress.GzipCompressor;
import org.example.codec.serialize.ProtostuffSerializer;
import org.example.common.Constants;
import org.example.protocol.MessageFrame;
import org.example.protocol.Request;
import org.example.protocol.Response;
import org.example.utils.GsonUtil;
import org.example.utils.SingletonFactory;

/**
 * @author chenqian
 * @date 2024/12/26 19:30
 **/
@Slf4j
public class MessageFrameDecoder extends LengthFieldBasedFrameDecoder {
    /**
     * @param maxFrameLength
     * @param lengthFieldOffset
     * @param lengthFieldLength
     * @param lengthAdjustment
     * @param initialBytesToStrip
     * @param failFast
     */
    public MessageFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    /**
     * 从第initialBytesToStrip个字节开始解析，解析长度为length+lengthAdjustment+lengthFieldLength
     * 从第0个字节开始解析，解析长度为length+4-4
     */
    public MessageFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 4, -4, 0, true);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        Object decoded = super.decode(ctx, in);
        if (decoded instanceof ByteBuf) {
            ByteBuf frame = ((ByteBuf) decoded);
            if (frame.readableBytes() >= Constants.HEAD_LENGTH) {
                try {
                    int messageLength = frame.readInt();
                    int requestId = frame.readInt();
                    byte messageType = frame.readByte();
                    byte serializeType = frame.readByte();
                    byte compressType = frame.readByte();

                    MessageFrame messageFrame = new MessageFrame();
                    messageFrame.setRequestId(requestId);
                    messageFrame.setMessageType(messageType);
                    messageFrame.setSerializeType(serializeType);
                    messageFrame.setCompressType(compressType);
                    log.info("decode messageFrame={}", GsonUtil.toJson(messageFrame));

                    int dataLength = messageLength - Constants.HEAD_LENGTH;
                    if (dataLength > 0) {
                        byte[] data = new byte[dataLength];
                        frame.readBytes(data);

                        GzipCompressor compressor = SingletonFactory.getInstance(GzipCompressor.class);
                        ProtostuffSerializer serializer = SingletonFactory.getInstance(ProtostuffSerializer.class);
                        // decompress
                        data = compressor.decompress(data);

                        // deserialize
                        if (messageFrame.getMessageType() == Constants.MESSAGE_TYPE_REQUEST) {
                            Request request = serializer.deserialize(data, Request.class);
                            messageFrame.setData(request);
                        } else {
                            Response response = serializer.deserialize(data, Response.class);
                            messageFrame.setData(response);
                        }
                    }
                    return messageFrame;
                }catch (Exception e) {

                }finally {
                    frame.release();
                }
            }
        }
        return decoded;
    }

}
