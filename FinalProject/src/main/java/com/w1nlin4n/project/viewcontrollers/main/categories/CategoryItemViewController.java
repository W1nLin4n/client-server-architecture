package com.w1nlin4n.project.viewcontrollers.main.categories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.dto.CategoryDto;
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

public class CategoryItemViewController {
    private CategoryDto categoryDto;

    @Setter
    private int categoryId;

    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private Label nameLabel;

    public void setupCategoryItem() throws Exception {
        Request request = new Request(HttpMethod.GET, "/category/" + categoryId, null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        categoryDto = mapper.readValue(response.getBody(), CategoryDto.class);
        nameLabel.setText(categoryDto.getName());
    }

    @FXML
    private void viewCategory() throws Exception {
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
