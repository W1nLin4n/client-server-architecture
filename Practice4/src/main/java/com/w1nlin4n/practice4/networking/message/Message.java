package com.w1nlin4n.practice4.networking.message;

import lombok.Value;

@Value
public class Message {
    MessageCommand command;
    int userId;
    String body;
}
