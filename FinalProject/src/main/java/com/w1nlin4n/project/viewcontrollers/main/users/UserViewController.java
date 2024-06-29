package com.w1nlin4n.project.viewcontrollers.main.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.dto.UserDto;
import com.w1nlin4n.project.networking.HttpCode;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.networking.message.Request;
import com.w1nlin4n.project.networking.message.Response;
import com.w1nlin4n.project.viewcontrollers.properties.Properties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class UserViewController {
    private UserDto userDto;

    @Setter
    private int userId;

    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label accessLevelLabel;

    public void setupUser() throws Exception {
        Request request = new Request(HttpMethod.GET, "/auth/" + userId, null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        userDto = mapper.readValue(response.getBody(), UserDto.class);
        usernameLabel.setText(userDto.getUsername());
        accessLevelLabel.setText(userDto.getAccessLevel());
    }

    @FXML
    private void deleteUser() throws Exception {
        Request requestMe = new Request(HttpMethod.GET, "/auth/me", null, properties.getAccessToken());
        Response responseMe = properties.getClient().send(requestMe);
        ObjectMapper mapper = new ObjectMapper();
        UserDto userMeDto = mapper.readValue(responseMe.getBody(), UserDto.class);

        if (userMeDto.getUsername().equals(userDto.getUsername())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("User deletion error");
            alert.setContentText("You can't delete your own account");
            alert.showAndWait();
            return;
        }

        Request request = new Request(HttpMethod.DELETE, "/auth/" + userId, null, properties.getAccessToken());
        Response response = properties.getClient().send(request);

        if (response.getCode() == HttpCode.OK) {
            returnBack();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("User deletion error");
            alert.setContentText("User deletion failed");
            alert.showAndWait();
        }

        returnBack();
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
