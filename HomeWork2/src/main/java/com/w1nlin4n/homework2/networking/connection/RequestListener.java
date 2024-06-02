package com.w1nlin4n.homework2.networking.connection;

public interface RequestListener {
    byte[] getRequest() throws InterruptedException;
    void stopListener();
}
