package org.example.multiReactor;


import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author chenqian
 * @date 2024/12/5 19:21
 **/
public class SubReactor implements Runnable {
    // 每个reactor一个selector
    final Selector selector;
    final ConcurrentLinkedQueue<SocketChannel> newConnections = new ConcurrentLinkedQueue<>();

    // 初始化selector
    public SubReactor() throws IOException {
        selector = Selector.open();
    }

    /**
     * 不直接注册而是先放到队列里
     * 1. Selector和SelectorKey不是线程安全的，在其他线程操作可能会引发线程安全问题
     * 2. 注册操作可能会阻塞，避免阻塞MainReactor线程
     * 3. 将注册行为都放到SubReactor的主流程中执行，让当前线程控制注册行为
     */
    public void register(SocketChannel socket) throws IOException {
        newConnections.offer(socket);
        // If another thread is currently blocked in an invocation of the select() or select(long) methods then that invocation will return immediately.
        // 如果当前线程正被select()阻塞，不执行wakeup()会导致新连接无法被及时注册，必须等到阻塞结束
        // 可能会死锁，性能下降，资源浪费
        selector.wakeup();
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                processNewConnections();
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    dispatch(key);
                }
                selected.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 将socket 绑定到channel上
    private void processNewConnections() throws IOException {
        SocketChannel socketChannel;
        while ((socketChannel = newConnections.poll()) != null) {
            socketChannel.configureBlocking(false);
            SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ);
            key.attach(new Handler(key));
        }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable) k.attachment();
        if (r != null) {
            r.run();
        }
    }
}
