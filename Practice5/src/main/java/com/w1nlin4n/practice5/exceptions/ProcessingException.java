package com.w1nlin4n.practice5.exceptions;

public class ProcessingException extends RuntimeException {
    public ProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
