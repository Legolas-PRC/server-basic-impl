package org.example.Http;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @author chenqian
 * @date 2024/11/18 19:53
 **/
public class HttpHandler {
    public void handle(SelectionKey selectionKey) throws IOException {
        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int readBytes = socketChannel.read(buffer);
        if (readBytes > 0) {
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String message = new String(bytes);

            // 回写数据
            ByteBuffer writeBuffer = ByteBuffer.wrap(("收到：" + message).getBytes());
            socketChannel.write(writeBuffer);

            InputStream inputStream = socketChannel.socket().getInputStream();

        }
    }
}
