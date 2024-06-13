package com.w1nlin4n.practice3.networking.connection;

import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
public class DefaultRequestListener implements RequestListener {
    private final Queue<byte[]> requests = new ArrayDeque<>();
    private final AtomicBoolean isRunning;

    @Override
    public byte[] getRequest() throws InterruptedException {
        synchronized (requests) {
            while (requests.isEmpty()) {
                if (!isRunning.get())
                    return null;
                requests.wait();
            }
            return requests.remove();
        }
    }

    public void addRequest(final byte[] request) {
        synchronized (requests) {
            requests.add(request);
            requests.notifyAll();
        }
    }

    public void stopListener() {
        synchronized (requests) {
            requests.clear();
            requests.notifyAll();
        }
    }
}
