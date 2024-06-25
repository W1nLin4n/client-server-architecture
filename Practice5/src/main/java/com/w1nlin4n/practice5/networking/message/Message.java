package com.w1nlin4n.practice5.networking.message;

import lombok.Value;

@Value
public class Message {
    MessageCommand command;
    int userId;
    String body;
}
