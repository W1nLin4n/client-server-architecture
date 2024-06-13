package com.w1nlin4n.practice3.networking.connection;

public interface RequestListener {
    byte[] getRequest() throws InterruptedException;
    void stopListener();
}
