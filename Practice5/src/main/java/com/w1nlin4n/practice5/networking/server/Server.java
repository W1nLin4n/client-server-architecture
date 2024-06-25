package com.w1nlin4n.practice5.networking.server;

import com.w1nlin4n.practice5.networking.connection.ServerConnection;
import com.w1nlin4n.practice5.networking.pipeline.Handler;
import com.w1nlin4n.practice5.networking.pipeline.Receiver;
import com.w1nlin4n.practice5.networking.pipeline.Sender;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
public class Server {
    private final int MAX_CLIENTS = 255;

    private final ExecutorService executor;
    private final Receiver receiver;
    private final Handler handler;
    private final Sender sender;
    private final AtomicInteger packetId;
    private final AtomicBoolean isRunning;
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final boolean[] clientIdAvailable;

    public Server(int port, ExecutorService executor, Receiver receiver, Handler handler, Sender sender, AtomicBoolean isRunning) throws IOException {
        this.executor = executor;
        this.receiver = receiver;
        this.handler = handler;
        this.sender = sender;
        packetId = new AtomicInteger(0);
        this.isRunning = isRunning;
        clientIdAvailable = new boolean[MAX_CLIENTS];
        for (int i = 0; i < MAX_CLIENTS; i++) {
            clientIdAvailable[i] = true;
        }

        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    public void start() throws InterruptedException, IOException {
        isRunning.set(true);

        while (isRunning.get()) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    executor.execute(new ServerConnection(socketChannel, receiver, handler, sender, packetId, clientIdAvailable));
                }
            }
        }

        serverSocketChannel.close();
        executor.shutdown();
    }

    public void stop() {
        isRunning.set(false);
        selector.wakeup();
    }
}
