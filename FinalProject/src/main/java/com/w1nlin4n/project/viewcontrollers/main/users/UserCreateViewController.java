package com.w1nlin4n.project.viewcontrollers.main.users;

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class UserCreateViewController {
    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private TextField usernameField;

    @FXML
    private ToggleGroup accessLevel;

    @FXML
    private TextField passwordField;

    @FXML
    private void createUser() throws Exception {
        UserDto userDto = UserDto
                .builder()
                .username(usernameField.getText())
                .accessLevel(((RadioButton) accessLevel.getSelectedToggle()).getText())
                .passwordHash(Hash.hash(passwordField.getText()))
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(userDto);
        Request request = new Request(HttpMethod.POST, "/auth", body, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        if (response.getCode() == HttpCode.CREATED) {
            returnBack();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("User creation error");
            alert.setContentText("Couldn't create such user");
            alert.showAndWait();
        }
    }

    @FXML
    private void returnBack() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/users/Users.fxml"));
        Parent root = fxmlLoader.load();
        UsersViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setupUsers();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }
}
