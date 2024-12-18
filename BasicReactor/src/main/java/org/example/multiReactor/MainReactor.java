package org.example.multiReactor;

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
 * @date 2024/12/5 17:48
 **/
public class MainReactor implements Runnable {
    final Selector selector;
    final ServerSocketChannel serverSocket;
    final SubReactor[] subReactors;
    int next = 0;

    public MainReactor(int port,int subReactorCount) throws IOException {
        this.selector = Selector.open();
        this.serverSocket = ServerSocketChannel.open();
        this.serverSocket.bind(new InetSocketAddress(port));
        this.serverSocket.configureBlocking(false);
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());

        // 初始化子reactor
        subReactors = new SubReactor[subReactorCount];
        for (int i = 0; i < subReactors.length; i++) {
            subReactors[i] = new SubReactor();
            new Thread(subReactors[i]).start();
        }
    }

    @Override
    public void run(){
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
        // 主reactor只会是Acceptor
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
                    subReactors[next++].register(socket);
                }
                if(++next == subReactors.length) {
                    next = 0;
                }
            } catch (IOException e) {

            }
        }
    }
}
