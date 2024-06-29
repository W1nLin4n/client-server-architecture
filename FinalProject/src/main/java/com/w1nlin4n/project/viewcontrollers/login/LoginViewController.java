package com.w1nlin4n.project.viewcontrollers.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.dto.LoginDto;
import com.w1nlin4n.project.networking.HttpCode;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.networking.message.Request;
import com.w1nlin4n.project.networking.message.Response;
import com.w1nlin4n.project.utility.Hash;
import com.w1nlin4n.project.viewcontrollers.properties.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lombok.Setter;

public class LoginViewController {
    @Setter
    private Properties properties;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private void submit() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String passwordHash = Hash.hash(password);

        LoginDto loginDto = LoginDto
                .builder()
                .username(username)
                .passwordHash(passwordHash)
                .build();

        ObjectMapper mapper = new ObjectMapper();

        String body = mapper.writeValueAsString(loginDto);
        Request request = new Request(HttpMethod.POST, "/auth/login", body, null);
        Response response = properties.getClient().send(request);

        if (response.getCode() == HttpCode.OK) {
            properties.setAccessToken(mapper.readValue(response.getBody(), String.class));
            properties.getClientApp().showMain();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Login error");
            alert.setContentText("Incorrect username or password");
            alert.showAndWait();
        }
    }
}
