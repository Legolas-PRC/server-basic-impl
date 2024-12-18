package org.example.multiReactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author chenqian
 * @date 2024/12/2 22:38
 **/
public class Handler implements Runnable {
    final SelectionKey key;
    final SocketChannel socket;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1, PROCESSING = 2;
    int state = READING;

    // 为什么共用一个线程池
    static ThreadPoolExecutor pool = new ThreadPoolExecutor(10, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100));

    public Handler(SelectionKey key) {
        this.key = key;
        this.socket = (SocketChannel) key.channel();
    }

    void process() {
        // 具体的处理逻辑

    }

    @Override
    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            }
        } catch (IOException e) {

        }
    }

    synchronized void read() throws IOException {
        socket.read(input);
        if (inputIsComplete()) {
            state = PROCESSING;
            pool.execute(new Processor());
        }
    }

    void send() throws IOException {
        socket.write(output);
        if (outputIsComplete()) {
            key.cancel();
        }
    }

    boolean inputIsComplete() {
        return false;
    }

    boolean outputIsComplete() {
        return false;
    }

    synchronized void processAndHandOff() {
        process();
        state = SENDING;
        key.interestOps(SelectionKey.OP_WRITE);
    }

    class Processor implements Runnable {
        @Override
        public void run() {
            processAndHandOff();
        }
    }
}