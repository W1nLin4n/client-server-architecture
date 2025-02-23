package com.w1nlin4n.homework2.controllers;

import com.w1nlin4n.homework2.networking.message.MessageCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Endpoint {
    MessageCommand command();
    Class<?> dto() default void.class;
}
