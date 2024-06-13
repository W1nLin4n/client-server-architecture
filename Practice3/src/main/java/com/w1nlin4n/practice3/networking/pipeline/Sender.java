package com.w1nlin4n.practice3.networking.pipeline;

import java.util.Arrays;

public class Sender {
    public void sendPacket(byte[] packet, byte address) {
        System.out.println("Sending to " + address + " : " + Arrays.toString(packet));
    }
}
