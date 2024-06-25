package com.w1nlin4n.practice5.networking.packet;

import com.w1nlin4n.practice5.networking.message.Message;
import lombok.Value;

@Value
public class Packet {
    byte sourceId;
    long packetId;
    Message message;
}
