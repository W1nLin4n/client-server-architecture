package com.w1nlin4n.practice3.serialization;

public interface Deserializer<T> {
    T deserialize(byte[] obj);
}
