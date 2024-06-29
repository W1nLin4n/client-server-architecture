package com.w1nlin4n.project.viewcontrollers.main.categories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.dto.CategoryDto;
import com.w1nlin4n.project.networking.HttpCode;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.networking.message.Request;
import com.w1nlin4n.project.networking.message.Response;
import com.w1nlin4n.project.viewcontrollers.properties.Properties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class CategoryCreateViewController {
    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private void createCategory() throws Exception {
        CategoryDto categoryDto = CategoryDto
                .builder()
                .name(nameField.getText())
                .description(descriptionField.getText())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(categoryDto);
        Request request = new Request(HttpMethod.POST, "/category", body, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        if (response.getCode() == HttpCode.CREATED) {
            returnBack();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Category creation error");
            alert.setContentText("Couldn't create such category");
            alert.showAndWait();
        }
    }

    @FXML
    private void returnBack() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/categories/Categories.fxml"));
        Parent root = fxmlLoader.load();
        CategoriesViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setupCategories();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }
}
