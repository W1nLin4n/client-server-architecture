package com.w1nlin4n.project.networking.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.w1nlin4n.project.controllers.AuthController;
import com.w1nlin4n.project.controllers.Controller;
import com.w1nlin4n.project.controllers.endpoint.Endpoint;
import com.w1nlin4n.project.controllers.endpoint.params.Body;
import com.w1nlin4n.project.controllers.endpoint.params.Path;
import com.w1nlin4n.project.controllers.security.AccessLevel;
import com.w1nlin4n.project.controllers.security.Security;
import com.w1nlin4n.project.controllers.security.params.Token;
import com.w1nlin4n.project.database.ProductsDB;
import com.w1nlin4n.project.dto.UserDto;
import com.w1nlin4n.project.exceptions.ProcessingException;
import com.w1nlin4n.project.networking.HttpCode;
import com.w1nlin4n.project.networking.HttpMethod;
import org.reflections.Reflections;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Handler implements HttpHandler {
    private final List<Method> endpoints;
    private final HashMap<Class<?>, Object> controllers;

    public Handler(ProductsDB productsDB) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        endpoints = new ArrayList<>();
        controllers = new HashMap<>();
        Reflections reflections = new Reflections("com.w1nlin4n.project");
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
            controllers.put(controllerInstance.getClass(), controllerInstance);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Endpoint.class)) {
                    endpoints.add(method);
                }
            }
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        HttpMethod method = HttpMethod.valueOf(exchange.getRequestMethod());
        String accessToken = exchange.getRequestHeaders().getFirst("Authorization");
        AccessLevel accessLevel = accessLevelFromToken(accessToken);

        List<Method> applicableEndpoints = findApplicableEndpoints(endpoints, path, method);
        Method usedEndpoint = findUsedEndpoint(applicableEndpoints);

        if (usedEndpoint == null) {
            exchange.sendResponseHeaders(HttpCode.NOT_FOUND.code, 0);
            exchange.close();
            return;
        }

        Endpoint endpointAnnotation = usedEndpoint.getAnnotation(Endpoint.class);

        if (usedEndpoint.isAnnotationPresent(Security.class)) {
            Security securityAnnotation = usedEndpoint.getAnnotation(Security.class);
            if (Arrays.stream(securityAnnotation.level()).noneMatch(level -> level == accessLevel)) {
                exchange.sendResponseHeaders(
                        accessLevel == AccessLevel.NON_USER ?
                            HttpCode.UNAUTHORIZED.code :
                            HttpCode.FORBIDDEN.code,
                        -1);
                exchange.close();
                return;
            }
        }

        Object[] processedParameters;
        try {
            processedParameters = processParameters(usedEndpoint, exchange.getRequestBody(), path, accessToken);
        } catch (ProcessingException e) {
            exchange.sendResponseHeaders(HttpCode.INTERNAL_SERVER_ERROR.code, -1);
            exchange.close();
            return;
        }

        Object responseBody;
        try {
            responseBody = usedEndpoint.invoke(controllers.get(usedEndpoint.getDeclaringClass()), processedParameters);
        } catch (IllegalAccessException e) {
            exchange.sendResponseHeaders(HttpCode.INTERNAL_SERVER_ERROR.code, -1);
            exchange.close();
            return;
        } catch (Exception e) {
            exchange.sendResponseHeaders(endpointAnnotation.onFailure().code, -1);
            exchange.close();
            return;
        }

        if (responseBody == null) {
            exchange.sendResponseHeaders(endpointAnnotation.onSuccess().code, -1);
            exchange.close();
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        String responseBodyString;
        try {
            responseBodyString = mapper.writeValueAsString(responseBody);
        } catch (Exception e) {
            exchange.sendResponseHeaders(HttpCode.INTERNAL_SERVER_ERROR.code, -1);
            exchange.close();
            return;
        }

        byte[] responseBodyBytes = responseBodyString.getBytes(StandardCharsets.UTF_8);

        exchange.sendResponseHeaders(endpointAnnotation.onSuccess().code, responseBodyBytes.length);
        exchange.getResponseBody().write(responseBodyBytes);
        exchange.close();
    }

    private AccessLevel accessLevelFromToken(String accessToken) {
        if (accessToken == null || accessToken.isEmpty())
            return AccessLevel.NON_USER;
        UserDto userDto = ((AuthController) controllers.get(AuthController.class)).me(accessToken);
        return AccessLevel.valueOf(userDto.getAccessLevel());
    }

    private List<Method> findApplicableEndpoints(List<Method> endpoints, String path, HttpMethod method) {
        List<Method> applicableEndpoints = new ArrayList<>();
        for (Method endpoint : endpoints) {
            Endpoint endpointAnnotation = endpoint.getAnnotation(Endpoint.class);
            if (endpointAnnotation.method() != method)
                continue;
            if(pathMatchesPattern(endpointAnnotation.path(), path))
                applicableEndpoints.add(endpoint);
        }
        return applicableEndpoints;
    }

    private Method findUsedEndpoint(List<Method> applicableEndpoints) {
        Method usedEndpoint = null;
        for (Method endpoint : applicableEndpoints) {
            if (usedEndpoint == null) {
                usedEndpoint = endpoint;
                continue;
            }

            Endpoint usedEndpointAnnotation = usedEndpoint.getAnnotation(Endpoint.class);
            Endpoint endpointAnnotation = endpoint.getAnnotation(Endpoint.class);
            if (patternSpecificity(endpointAnnotation.path()) > patternSpecificity(usedEndpointAnnotation.path()))
                usedEndpoint = endpoint;
        }
        return usedEndpoint;
    }

    private boolean pathMatchesPattern(String pattern, String path) {
        String[] partsOfPattern = pattern.split("/");
        String[] partsOfPath = path.split("/");
        if (partsOfPattern.length != partsOfPath.length)
            return false;

        for (int i = 0; i < partsOfPattern.length; i++) {
            if(
                    partsOfPattern[i].equals(partsOfPath[i]) ||
                    (partsOfPattern[i].startsWith("{") && partsOfPattern[i].endsWith("}"))
            )
                continue;
            return false;
        }
        return true;
    }

    private int patternSpecificity(String pattern) {
        int count = 0;
        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '{')
                count--;
        }
        return count;
    }

    private Object[] processParameters(Method usedEndpoint, InputStream body, String path, String accessToken) {
        Endpoint usedEndpointAnnotation = usedEndpoint.getAnnotation(Endpoint.class);
        Annotation[][] parameterAnnotations = usedEndpoint.getParameterAnnotations();
        Class<?>[] parameterTypes = usedEndpoint.getParameterTypes();
        Parameter[] parameters = usedEndpoint.getParameters();
        Object[] processedParameters = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation instanceof Path) {
                    String nameInPath = ((Path) annotation).name();
                    try {
                        processedParameters[i] = processPathParameter(usedEndpointAnnotation.path(), path, parameterTypes[i], nameInPath);
                    } catch (Exception e) {
                        throw new ProcessingException("Could not process path parameter", e);
                    }
                }

                if (annotation instanceof Body) {
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        processedParameters[i] = mapper.readValue(body, parameterTypes[i]);
                    } catch (Exception e) {
                        throw new ProcessingException("Could not process request body", e);
                    }
                }

                if (annotation instanceof Token) {
                    processedParameters[i] = accessToken;
                }
            }
        }

        return processedParameters;
    }

    private Object processPathParameter(String pattern, String path, Class<?> clazz, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] partsOfPattern = pattern.split("/");
        String[] partsOfPath = path.split("/");

        for (int i = 0; i < partsOfPattern.length; i++) {
            if (partsOfPattern[i].equals("{" + name + "}"))
                return clazz.getMethod("valueOf", String.class).invoke(null, partsOfPath[i]);
        }

        return null;
    }
}
