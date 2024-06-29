package com.w1nlin4n.project;

import com.w1nlin4n.project.database.ProductsDB;
import com.w1nlin4n.project.networking.server.Handler;
import com.w1nlin4n.project.networking.server.Server;
import com.w1nlin4n.project.utility.ArgsParser;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class ServerApp {
    private static Server server;

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, InterruptedException {
        HashMap<String, String> parsedArgs = ArgsParser.parse(args);
        int port = 3333;
        if (parsedArgs.containsKey("-p")) {
            port = Integer.parseInt(parsedArgs.get("-p"));
        }
        String dbUrl = "jdbc:sqlite::memory:";
        if (parsedArgs.containsKey("-db")) {
            dbUrl = parsedArgs.get("-db");
        }
        ProductsDB productsDB = new ProductsDB(dbUrl);
        Handler handler = new Handler(productsDB);
        server = new Server(port, Executors.newCachedThreadPool(), handler);
        server.start();
    }
}
