package org.example.basicImpl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author chenqian
 * @date 2024/11/20 11:18
 **/
public class Reactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel serverSocket;

    Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(port));
        serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    dispatch(it.next());
                }
                selected.clear();
            }
        } catch (IOException e) {

        }
    }

    void dispatch(SelectionKey k) {
        // 可能是Acceptor,也可能是handler
        Runnable r = (Runnable) k.attachment();
        if(r!= null) {
            r.run();
        }
    }

    class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel socket = serverSocket.accept();
                if (socket != null) {
                    new Handler(selector, socket);
                }
            } catch (IOException e) {

            }
        }
    }
}
