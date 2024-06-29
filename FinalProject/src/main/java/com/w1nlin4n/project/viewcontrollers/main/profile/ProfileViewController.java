package com.w1nlin4n.project.viewcontrollers.main.profile;

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

public class ProfileViewController {
    private UserDto userDto;

    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label accessLevelLabel;

    public void setupProfile() throws Exception {
        Request request = new Request(HttpMethod.GET, "/auth/me", null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        userDto = mapper.readValue(response.getBody(), UserDto.class);
        usernameLabel.setText(userDto.getUsername());
        accessLevelLabel.setText(userDto.getAccessLevel());
    }

    @FXML
    private void modifyProfile() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/profile/ProfileUpdate.fxml"));
        Parent root = fxmlLoader.load();
        ProfileUpdateViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setupProfileUpdate();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }
}
