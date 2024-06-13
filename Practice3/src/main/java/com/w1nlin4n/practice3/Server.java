package com.w1nlin4n.practice3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.practice3.cryptography.CryptographyHandler;
import com.w1nlin4n.practice3.cryptography.RedundancyCheckHandler;
import com.w1nlin4n.practice3.database.ProductsDB;
import com.w1nlin4n.practice3.dto.ProductAmountChangeDto;
import com.w1nlin4n.practice3.entities.Category;
import com.w1nlin4n.practice3.entities.Product;
import com.w1nlin4n.practice3.networking.connection.DefaultRequestListener;
import com.w1nlin4n.practice3.networking.message.Message;
import com.w1nlin4n.practice3.networking.message.MessageCommand;
import com.w1nlin4n.practice3.networking.packet.Packet;
import com.w1nlin4n.practice3.networking.pipeline.Handler;
import com.w1nlin4n.practice3.networking.pipeline.Receiver;
import com.w1nlin4n.practice3.networking.connection.RequestListener;
import com.w1nlin4n.practice3.networking.pipeline.Sender;
import com.w1nlin4n.practice3.serialization.DefaultPacketDeserializer;
import com.w1nlin4n.practice3.serialization.DefaultPacketSerializer;
import com.w1nlin4n.practice3.serialization.Deserializer;
import com.w1nlin4n.practice3.serialization.Serializer;
import com.w1nlin4n.practice3.cryptography.RedundancyCheckHandler;
import com.w1nlin4n.practice3.serialization.DefaultPacketDeserializer;
import com.w1nlin4n.practice3.serialization.DefaultPacketSerializer;
import com.w1nlin4n.practice3.serialization.Deserializer;
import com.w1nlin4n.practice3.serialization.Serializer;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@AllArgsConstructor
public class Server {
    private final ExecutorService executor;
    private final RequestListener requestListener;
    private final Receiver receiver;
    private final AtomicBoolean isRunning;

    public void start() throws InterruptedException {
        isRunning.set(true);
        while (isRunning.get()) {
            byte[] request = requestListener.getRequest();
            if (request == null)
                break;
            executor.execute(() -> receiver.receivePacket(request));
        }
        executor.shutdown();
    }

    public void stop() {
        isRunning.set(false);
        requestListener.stopListener();
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ProductsDB productsDB;
        ExecutorService executorService;
        AtomicBoolean isRunning;
        DefaultRequestListener requestListener;
        CryptographyHandler cryptographyHandler;
        RedundancyCheckHandler redundancyCheckHandler;
        Serializer<Packet> packetSerializer;
        Deserializer<Packet> packetDeserializer;
        Handler handler;
        Sender sender;
        Receiver receiver;
        Server server;
        productsDB = new ProductsDB();
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
        requestListener = new DefaultRequestListener(isRunning);
        cryptographyHandler = new CryptographyHandler();
        redundancyCheckHandler = new RedundancyCheckHandler();
        packetSerializer = new DefaultPacketSerializer(cryptographyHandler, redundancyCheckHandler);
        packetDeserializer = new DefaultPacketDeserializer(cryptographyHandler, redundancyCheckHandler);
        handler = new Handler(productsDB);
        sender = new Sender();
        receiver = new Receiver(packetDeserializer, packetSerializer, handler, sender);
        server = new Server(executorService, requestListener, receiver, isRunning);

    }
}
