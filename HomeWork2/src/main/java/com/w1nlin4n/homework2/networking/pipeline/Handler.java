package com.w1nlin4n.homework2.networking.pipeline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.homework2.controllers.Controller;
import com.w1nlin4n.homework2.controllers.Endpoint;
import com.w1nlin4n.homework2.database.ProductsDB;
import com.w1nlin4n.homework2.networking.message.Message;
import com.w1nlin4n.homework2.networking.message.MessageCommand;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;


public class Handler {
    private final HashMap<MessageCommand, Method> endpoints;
    private final HashMap<MessageCommand, Object> controllers;

    public Handler(ProductsDB productsDB) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        endpoints = new HashMap<>();
        controllers = new HashMap<>();
        Reflections reflections = new Reflections("com.w1nlin4n.homework2");
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Controller.class);
        for (Class<?> clazz : classes) {
            Controller controller = clazz.getAnnotation(Controller.class);
            Object controllerInstance =
                    clazz
                            .getDeclaredConstructor(controller.service())
                            .newInstance(
                                    controller
                                            .service()
                                            .getDeclaredConstructor(ProductsDB.class)
                                            .newInstance(productsDB)
                            );
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Endpoint.class)) {
                    Endpoint endpoint = method.getAnnotation(Endpoint.class);
                    endpoints.put(endpoint.command(), method);
                    controllers.put(endpoint.command(), controllerInstance);
                }
            }
        }
    }

    public Message handleMessage(Message message) throws InvocationTargetException, IllegalAccessException, JsonProcessingException {
        Method method = endpoints.get(message.getCommand());
        Object controllerInstance = controllers.get(message.getCommand());
        if (method == null || controllerInstance == null)
            return new Message(MessageCommand.ERROR, message.getUserId(), "");
        ObjectMapper objectMapper = new ObjectMapper();
        Endpoint endpoint = method.getAnnotation(Endpoint.class);
        Object returnResult = method.invoke(controllerInstance,
                objectMapper.readValue(message.getBody(), endpoint.dto())
        );
        return new Message(MessageCommand.INFORMATION, message.getUserId(), objectMapper.writeValueAsString(returnResult));
    }
}
