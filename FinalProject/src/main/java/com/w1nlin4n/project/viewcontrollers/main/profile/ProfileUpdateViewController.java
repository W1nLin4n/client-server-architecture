package com.w1nlin4n.project.viewcontrollers.main.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.dto.UserDto;
import com.w1nlin4n.project.networking.HttpCode;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.networking.message.Request;
import com.w1nlin4n.project.networking.message.Response;
import com.w1nlin4n.project.utility.Hash;
import com.w1nlin4n.project.viewcontrollers.properties.Properties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class ProfileUpdateViewController {
    private UserDto userDto;

    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    public void setupProfileUpdate() throws Exception {
        Request request = new Request(HttpMethod.GET, "/auth/me", null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        userDto = mapper.readValue(response.getBody(), UserDto.class);
        usernameField.setText(userDto.getUsername());
    }

    @FXML
    private void applyChanges() throws Exception {
        UserDto requestUserDto = UserDto
                .builder()
                .username(usernameField.getText())
                .passwordHash(Hash.hash(passwordField.getText()))
                .accessLevel(userDto.getAccessLevel())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        Request request = new Request(HttpMethod.PUT, "/auth/me", mapper.writeValueAsString(requestUserDto), properties.getAccessToken());
        Response response = properties.getClient().send(request);

        if (response.getCode() == HttpCode.OK) {
            properties.setAccessToken(null);
            properties.getClientApp().showLogin();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Profile update error");
            alert.setContentText("Profile update failed");
            alert.showAndWait();
        }
    }

    @FXML
    private void returnBack() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/profile/Profile.fxml"));
        Parent root = fxmlLoader.load();
        ProfileViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setupProfile();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }
}
