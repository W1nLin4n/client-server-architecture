package com.w1nlin4n.project.viewcontrollers.main.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.dto.UserDto;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.networking.message.Request;
import com.w1nlin4n.project.networking.message.Response;
import com.w1nlin4n.project.viewcontrollers.properties.Properties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class UserItemViewController {
    private UserDto userDto;

    @Setter
    private int userId;

    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private Label usernameLabel;

    public void setupUserItem() throws Exception {
        Request request = new Request(HttpMethod.GET, "/auth/" + userId, null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        userDto = mapper.readValue(response.getBody(), UserDto.class);
        usernameLabel.setText(userDto.getUsername());
    }

    @FXML
    private void viewUser() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/users/User.fxml"));
        Parent root = fxmlLoader.load();
        UserViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setUserId(userId);
        controller.setupUser();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }
}
