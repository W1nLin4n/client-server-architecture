package com.w1nlin4n.homework2.networking.message;

import lombok.Value;

@Value
public class Message {
    int command;
    int userId;
    String body;
}
