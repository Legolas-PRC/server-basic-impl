package org.example.basicImpl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author chenqian
 * @date 2024/11/20 11:31
 **/
public final class Handler implements Runnable {
    final SocketChannel socket;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0, SENDING = 1;
    int state = READING;

    Handler(Selector selector, SocketChannel c) throws IOException {
        socket = c;
        c.configureBlocking(false);
        sk = socket.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    boolean inputIsComplete() {
        return false;
    }

    boolean outputIsComplete() {
        return false;
    }

    void process() {
        System.out.println("process");
    }

    @Override
    public void run() {
        try {
            if(state == READING){
                read();
            }else if (state == SENDING){
                send();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void read() throws IOException {
        socket.read(input);
        if (inputIsComplete()) {
            process();
            state = SENDING;
            sk.interestOps(SelectionKey.OP_WRITE);
        }
    }

    void send() throws IOException {
        socket.write(output);
        if (outputIsComplete()) {
            sk.cancel();
        }
    }
}
