package org.example.impl;

import org.example.Server;
import org.example.utils.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * NIO 同步非阻塞
 * 非阻塞，允许连接数据没有准备好时立即返回，不会阻塞线程
 * @author chenqian
 * @date 2024/11/18 16:59
 **/
public class NIOServerImpl implements Server {
    private static Logger logger = LoggerFactory.getLogger(NIOServerImpl.class);
    @Override
    public void start() {
        System.out.println("Server started.");
        try {
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10,
                    10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

            int availablePort = NetUtils.findAvailablePort(10010);
            logger.info(">>>>>>available port is:{}", availablePort);
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(availablePort));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            // Reactor:监听连接事件并分发
            while (true) {
                // 阻塞，直到至少有一个channel可IO
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                // 循环处理每一个IO事件
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isAcceptable()) {
                        // 有新的socket连接，将连接注册到selector
                        // Acceptor接收连接
                        ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = serverChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Accepted connection from " + socketChannel.getRemoteAddress());
                    }else if(selectionKey.isReadable()) {
                        // socket连接有数据
                        // handler在主线程里，只负责读和写数据，不参与业务处理，业务交给线程池
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int readBytes = socketChannel.read(buffer);
                        if (readBytes > 0) {
                            buffer.flip();
                            byte[] bytes = new byte[buffer.remaining()];
                            buffer.get(bytes);
                            String message = new String(bytes);
                            System.out.println("收到消息：" + message);
                            // 回写数据
                            ByteBuffer writeBuffer = ByteBuffer.wrap(("收到：" + message).getBytes());
                            socketChannel.write(writeBuffer);
                        }
                    }

                }
            }
        } catch (IOException e) {

        }
    }
}
