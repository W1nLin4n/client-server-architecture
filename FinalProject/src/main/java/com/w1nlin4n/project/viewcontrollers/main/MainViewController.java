package com.w1nlin4n.project.viewcontrollers.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.controllers.security.AccessLevel;
import com.w1nlin4n.project.dto.UserDto;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.networking.message.Request;
import com.w1nlin4n.project.networking.message.Response;
import com.w1nlin4n.project.viewcontrollers.main.categories.CategoriesViewController;
import com.w1nlin4n.project.viewcontrollers.main.products.ProductsViewController;
import com.w1nlin4n.project.viewcontrollers.main.profile.ProfileViewController;
import com.w1nlin4n.project.viewcontrollers.main.users.UsersViewController;
import com.w1nlin4n.project.viewcontrollers.properties.Properties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class MainViewController {
    @Setter
    private Properties properties;

    @FXML
    private Pane contentPane;

    @FXML
    private Button usersButton;

    @FXML
    private Button categoriesButton;

    @FXML
    private Button productsButton;

    @FXML
    private void profile() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/profile/Profile.fxml"));
        Parent root = fxmlLoader.load();
        ProfileViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setupProfile();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }

    @FXML
    private void users() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/users/Users.fxml"));
        Parent root = fxmlLoader.load();
        UsersViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setupUsers();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }

    @FXML
    private void categories() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/categories/Categories.fxml"));
        Parent root = fxmlLoader.load();
        CategoriesViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setupCategories();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }

    @FXML
    private void products() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/products/Products.fxml"));
        Parent root = fxmlLoader.load();
        ProductsViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setCategoryId(null);
        controller.setupProducts();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }

    @FXML
    private void logout() throws Exception {
        properties.setAccessToken(null);
        properties.getClientApp().showLogin();
    }

    public void setupMain() throws Exception {
        Request request = new Request(HttpMethod.GET, "/auth/me", null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        UserDto userDto = mapper.readValue(response.getBody(), UserDto.class);

        switch (AccessLevel.valueOf(userDto.getAccessLevel())) {
            case ADMIN:
                categoriesButton.setManaged(false);
                categoriesButton.setVisible(false);
                productsButton.setManaged(false);
                productsButton.setVisible(false);
                break;
            case USER:
                usersButton.setManaged(false);
                usersButton.setVisible(false);
                break;
        }

        contentPane.getChildren().clear();
    }
}
