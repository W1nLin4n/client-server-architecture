package com.w1nlin4n.practice3.networking.message;

import lombok.Value;

@Value
public class Message {
    MessageCommand command;
    int userId;
    String body;
}
