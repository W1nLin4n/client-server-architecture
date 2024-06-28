package com.w1nlin4n.project.controllers.endpoint;

import com.w1nlin4n.project.networking.HttpCode;
import com.w1nlin4n.project.networking.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Endpoint {
    String path() default "/";
    HttpMethod method();
    HttpCode onSuccess() default HttpCode.OK;
    HttpCode onFailure() default HttpCode.INTERNAL_SERVER_ERROR;
}
