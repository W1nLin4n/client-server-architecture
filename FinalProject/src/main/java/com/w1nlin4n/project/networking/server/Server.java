package com.w1nlin4n.project.networking.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

public class Server {
    private final ExecutorService executor;
    private final HttpServer server;

    public Server(int port, ExecutorService executor, Handler handler) throws IOException {
        this.executor = executor;
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(executor);
        server.createContext("/", handler);
    }

    public void start() throws InterruptedException, IOException {
        server.start();
    }

    public void stop() {
        server.stop(20);
        executor.shutdown();
    }
}
