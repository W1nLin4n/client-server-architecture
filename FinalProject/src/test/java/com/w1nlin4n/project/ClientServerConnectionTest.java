package com.w1nlin4n.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.controllers.security.AccessLevel;
import com.w1nlin4n.project.database.ProductsDB;
import com.w1nlin4n.project.dto.LoginDto;
import com.w1nlin4n.project.dto.UserDto;
import com.w1nlin4n.project.entities.Category;
import com.w1nlin4n.project.entities.Product;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.networking.client.Client;
import com.w1nlin4n.project.networking.message.Request;
import com.w1nlin4n.project.networking.message.Response;
import com.w1nlin4n.project.networking.server.Handler;
import com.w1nlin4n.project.networking.server.Server;
import com.w1nlin4n.project.services.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ClientServerConnectionTest {
    ProductsDB productsDB;
    ExecutorService executorService;
    Handler handler;
    Server server;

    @BeforeEach
    void setUp() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        productsDB = new ProductsDB("jdbc:sqlite::memory:");
        Category category1 = Category
                .builder()
                .name("Food")
                .description("these are what people eat")
                .build();
        Category category2 = Category
                .builder()
                .name("Drinks")
                .description("these are what people drink")
                .build();
        Product product1 = Product
                .builder()
                .name("Pizza")
                .description("italian food")
                .manufacturer("Italy")
                .amount(20)
                .price(12.5)
                .build();
        Product product2 = Product
                .builder()
                .name("Sushi")
                .description("japanese food")
                .manufacturer("Japan")
                .amount(10)
                .price(10.)
                .build();
        Product product3 = Product
                .builder()
                .name("Wine")
                .description("alcohol drink")
                .manufacturer("France")
                .amount(30)
                .price(13.)
                .build();
        Product product4 = Product
                .builder()
                .name("Water")
                .description("regular drink")
                .manufacturer("Ukraine")
                .amount(1000)
                .price(5.)
                .build();
        productsDB.createCategory(category1);
        productsDB.createCategory(category2);
        Integer category1Id = productsDB.getCategoryByName("Food").getId();
        Integer category2Id = productsDB.getCategoryByName("Drinks").getId();
        product1.setCategoryId(category1Id);
        product2.setCategoryId(category1Id);
        product3.setCategoryId(category2Id);
        product4.setCategoryId(category2Id);
        productsDB.createProduct(product1);
        productsDB.createProduct(product2);
        productsDB.createProduct(product3);
        productsDB.createProduct(product4);
        AuthService authService = new AuthService(productsDB);
        UserDto userDto = UserDto
                .builder()
                .username("user")
                .passwordHash("8AC76453D769D4FD14B3F41AD4933F9BD64321972CD002DE9B847E117435B08B")
                .accessLevel(AccessLevel.USER.name())
                .build();
        authService.createUser(userDto);
        executorService = Executors.newCachedThreadPool();
        handler = new Handler(productsDB);
        server = new Server(3333, executorService, handler);
    }

    @Test
    void manyRequestsTest() throws InterruptedException, IOException {
        Thread serverThread = new Thread(() -> {
            try {
                server.start();
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();
        List<Thread> threads = new ArrayList<>();
        List<Client> clients = new ArrayList<>();
        for(byte i = 0; i < 10; i++) {
            Client client = new Client("localhost", 3000 + i, "localhost", 3333);
            clients.add(client);
            threads.add(new Thread(() -> {
                client.start();
                ObjectMapper objectMapper = new ObjectMapper();
                LoginDto loginDto = new LoginDto("user", "8AC76453D769D4FD14B3F41AD4933F9BD64321972CD002DE9B847E117435B08B");
                String loginBody;
                try {
                    loginBody = objectMapper.writeValueAsString(loginDto);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                Request loginRequest = new Request(HttpMethod.POST, "/auth/login", loginBody, null);
                Response loginResponse;
                try {
                    loginResponse = client.send(loginRequest);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String accessToken;
                try {
                    accessToken = objectMapper.readValue(loginResponse.getBody(), String.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for(byte j = 0; j < 10; j++) {
                    Request request = new Request(HttpMethod.PUT, "/product/" + productsDB.getProductByName("Water").getId() + "/add/10", null, accessToken);
                    try {
                        client.send(request);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
        }
        for(byte i = 0; i < 10; i++) {
            Client client = new Client("localhost", 3010 + i, "localhost", 3333);
            clients.add(client);
            threads.add(new Thread(() -> {
                client.start();
                ObjectMapper objectMapper = new ObjectMapper();
                LoginDto loginDto = new LoginDto("user", "8AC76453D769D4FD14B3F41AD4933F9BD64321972CD002DE9B847E117435B08B");
                String loginBody;
                try {
                    loginBody = objectMapper.writeValueAsString(loginDto);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                Request loginRequest = new Request(HttpMethod.POST, "/auth/login", loginBody, null);
                Response loginResponse;
                try {
                    loginResponse = client.send(loginRequest);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String accessToken;
                try {
                    accessToken = objectMapper.readValue(loginResponse.getBody(), String.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for(byte j = 0; j < 10; j++) {
                    Request request = new Request(HttpMethod.PUT, "/product/" + productsDB.getProductByName("Water").getId() + "/remove/10", null, accessToken);
                    try {
                        client.send(request);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
        }
        for(Thread thread : threads) {
            thread.start();
        }
        for(Thread thread : threads) {
            thread.join();
        }
        synchronized (this) {
            wait(2000);
        }
        for (Client client : clients) {
            client.stop();
        }
        server.stop();
        Assertions.assertEquals(1000, productsDB.getProductByName("Water").getAmount());
    }

    @AfterEach
    void tearDown() {
        productsDB = null;
        executorService = null;
        handler = null;
        server = null;
    }
}