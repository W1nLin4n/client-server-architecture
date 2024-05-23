package com.w1nlin4n.practice1.networking.message;

import lombok.Value;

@Value
public class Message {
    int command;
    int userId;
    String body;
}
