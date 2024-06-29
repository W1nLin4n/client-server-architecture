package com.w1nlin4n.project;

import com.w1nlin4n.project.networking.client.Client;
import com.w1nlin4n.project.utility.ArgsParser;
import com.w1nlin4n.project.viewcontrollers.main.MainViewController;
import com.w1nlin4n.project.viewcontrollers.properties.Properties;
import com.w1nlin4n.project.viewcontrollers.login.LoginViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class ClientApp extends Application {
    private static Client client;
    private Stage stage;
    private Properties properties;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        properties = Properties
                .builder()
                .client(client)
                .clientApp(this)
                .build();
        showLogin();
    }

    public void showLogin() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/login/Login.fxml"));
        Parent root = fxmlLoader.load();
        LoginViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();
    }

    public void showMain() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/Main.fxml"));
        Parent root = fxmlLoader.load();
        MainViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setupMain();
        stage.setScene(new Scene(root));
        stage.setTitle("Main");
        stage.show();
    }

    public static void main(String[] args) {
        HashMap<String, String> parsedArgs = ArgsParser.parse(args);
        String clientAddress = "localhost";
        if (parsedArgs.containsKey("-ca")) {
            clientAddress = parsedArgs.get("-ca");
        }
        int clientPort = 3000;
        if (parsedArgs.containsKey("-cp")) {
            clientPort = Integer.parseInt(parsedArgs.get("-cp"));
        }
        String serverAddress = "localhost";
        if (parsedArgs.containsKey("-sa")) {
            clientAddress = parsedArgs.get("-sa");
        }
        int serverPort = 3333;
        if (parsedArgs.containsKey("-sp")) {
            serverPort = Integer.parseInt(parsedArgs.get("-sp"));
        }
        client = new Client(clientAddress, clientPort, serverAddress, serverPort);
        client.start();
        launch(args);
    }
}
