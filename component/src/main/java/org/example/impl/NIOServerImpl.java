package org.example.impl;

import org.example.Server;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * NIO 同步非阻塞
 * 非阻塞，允许连接数据没有准备好时立即返回，不会阻塞线程
 * @author chenqian
 * @date 2024/11/18 16:59
 **/
/**
 * @param null:
 * @return null
 * @author LiaoMoran
 * @description TODO
 * @date 2025/1/14 15:16
 */
public class NIOServerImpl implements Server {
    @Override
    public void start() {
        System.out.println("Server started.");
        try {
            ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(10010));
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
