package com.w1nlin4n.practice4;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.practice4.cryptography.CryptographyHandler;
import com.w1nlin4n.practice4.cryptography.RedundancyCheckHandler;
import com.w1nlin4n.practice4.database.ProductsDB;
import com.w1nlin4n.practice4.dto.ProductAmountChangeDto;
import com.w1nlin4n.practice4.entities.Category;
import com.w1nlin4n.practice4.entities.Product;
import com.w1nlin4n.practice4.networking.client.Client;
import com.w1nlin4n.practice4.networking.message.Message;
import com.w1nlin4n.practice4.networking.message.MessageCommand;
import com.w1nlin4n.practice4.networking.packet.Packet;
import com.w1nlin4n.practice4.networking.pipeline.Handler;
import com.w1nlin4n.practice4.networking.pipeline.Receiver;
import com.w1nlin4n.practice4.networking.pipeline.Sender;
import com.w1nlin4n.practice4.networking.server.Server;
import com.w1nlin4n.practice4.serialization.DefaultPacketDeserializer;
import com.w1nlin4n.practice4.serialization.DefaultPacketSerializer;
import com.w1nlin4n.practice4.serialization.Deserializer;
import com.w1nlin4n.practice4.serialization.Serializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class ClientServerConnectionTest {
    ProductsDB productsDB;
    ExecutorService executorService;
    AtomicBoolean isRunning = new AtomicBoolean(false);
    CryptographyHandler cryptographyHandler;
    RedundancyCheckHandler redundancyCheckHandler;
    Serializer<Packet> packetSerializer;
    Deserializer<Packet> packetDeserializer;
    Handler handler;
    Sender sender;
    Receiver receiver;
    Server server;

    @BeforeEach
    void setUp() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        productsDB = new ProductsDB("jdbc:sqlite::memory:");
        Category category1 = new Category("Food", "these are what people eat");
        Category category2 = new Category("Drinks", "these are what people drink");
        Product product1 = new Product("Pizza", "italian food", "Italy", 20, 12.5);
        Product product2 = new Product("Sushi", "japanese food", "Japan", 10, 10.);
        Product product3 = new Product("Wine", "alcohol drink", "France", 30, 13.);
        Product product4 = new Product("Water", "regular drink", "Ukraine", 1000, 5.);
        productsDB.createCategory(category1);
        productsDB.createCategory(category2);
        productsDB.createProduct(product1);
        productsDB.createProduct(product2);
        productsDB.createProduct(product3);
        productsDB.createProduct(product4);
        productsDB.addProductToCategory("Pizza", "Food");
        productsDB.addProductToCategory("Sushi", "Food");
        productsDB.addProductToCategory("Wine", "Drinks");
        productsDB.addProductToCategory("Water", "Drinks");
        executorService = Executors.newCachedThreadPool();
        isRunning = new AtomicBoolean(false);
        cryptographyHandler = new CryptographyHandler();
        redundancyCheckHandler = new RedundancyCheckHandler();
        packetSerializer = new DefaultPacketSerializer(cryptographyHandler, redundancyCheckHandler);
        packetDeserializer = new DefaultPacketDeserializer(cryptographyHandler, redundancyCheckHandler);
        handler = new Handler(productsDB);
        sender = new Sender(packetSerializer);
        receiver = new Receiver(packetDeserializer);
        server = new Server(3333, executorService, receiver, handler, sender, isRunning);
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
            Client client = new Client("localhost", 3000 + i, "localhost", 3333, packetSerializer, receiver, sender);
            clients.add(client);
            threads.add(new Thread(() -> {
                try {
                    client.start();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ObjectMapper objectMapper = new ObjectMapper();
                for(byte j = 0; j < 10; j++) {
                    Message message;
                    try {
                        message = new Message(
                                MessageCommand.ADD_PRODUCT_AMOUNT,
                                j,
                                objectMapper.writeValueAsString(
                                        ProductAmountChangeDto
                                                .builder()
                                                .productAmount(10)
                                                .productName("Water")
                                                .build()
                                )
                        );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        client.sendMessage(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }));
        }
        for(byte i = 0; i < 10; i++) {
            Client client = new Client("localhost", 3010 + i, "localhost", 3333, packetSerializer, receiver, sender);
            clients.add(client);
            threads.add(new Thread(() -> {
                try {
                    client.start();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                ObjectMapper objectMapper = new ObjectMapper();
                for(byte j = 0; j < 10; j++) {
                    Message message;
                    try {
                        message = new Message(
                                MessageCommand.REMOVE_PRODUCT_AMOUNT,
                                j,
                                objectMapper.writeValueAsString(
                                        ProductAmountChangeDto
                                                .builder()
                                                .productAmount(10)
                                                .productName("Water")
                                                .build()
                                )
                        );
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        client.sendMessage(message);
                    } catch (IOException e) {
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
            client.close();
        }
        server.stop();
        assertEquals(1000, productsDB.getProduct("Water").getAmount());
    }

    @AfterEach
    void tearDown() {
        productsDB = null;
        executorService = null;
        cryptographyHandler = null;
        redundancyCheckHandler = null;
        packetSerializer = null;
        packetDeserializer = null;
        handler = null;
        sender = null;
        receiver = null;
        server = null;
    }
}