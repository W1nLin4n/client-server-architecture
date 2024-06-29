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

public class CategoryUpdateViewController {
    private CategoryDto categoryDto;

    @Setter
    private int categoryId;

    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    public void setupCategoryUpdate() throws Exception {
        Request request = new Request(HttpMethod.GET, "/category/" + categoryId, null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        categoryDto = mapper.readValue(response.getBody(), CategoryDto.class);
        nameField.setText(categoryDto.getName());
        descriptionField.setText(categoryDto.getDescription());
    }

    @FXML
    private void applyChanges() throws Exception {
        CategoryDto requestCategoryDto = CategoryDto
                .builder()
                .name(nameField.getText())
                .description(descriptionField.getText())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        Request request = new Request(HttpMethod.PUT, "/category/" + categoryId, mapper.writeValueAsString(requestCategoryDto), properties.getAccessToken());
        Response response = properties.getClient().send(request);

        if (response.getCode() == HttpCode.OK) {
            returnBack();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Category update error");
            alert.setContentText("Category update failed");
            alert.showAndWait();
        }
    }

    @FXML
    private void returnBack() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/categories/Category.fxml"));
        Parent root = fxmlLoader.load();
        CategoryViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setCategoryId(categoryId);
        controller.setupCategory();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }
}
