package com.w1nlin4n.practice5.networking;

public enum HttpCode {
    OK(200),
    CREATED(201),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500);

    public final int code;

    HttpCode(int code) {
        this.code = code;
    }

    public static HttpCode fromCode(int code) {
        for (HttpCode httpCode : HttpCode.values()) {
            if (httpCode.code == code) {
                return httpCode;
            }
        }
        return null;
    }
}
