package com.w1nlin4n.project.viewcontrollers.main.categories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.dto.CategoryDto;
import com.w1nlin4n.project.networking.HttpCode;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.networking.message.Request;
import com.w1nlin4n.project.networking.message.Response;
import com.w1nlin4n.project.viewcontrollers.main.products.ProductsViewController;
import com.w1nlin4n.project.viewcontrollers.properties.Properties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class CategoryViewController {
    private CategoryDto categoryDto;

    @Setter
    private int categoryId;

    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Label descriptionLabel;

    public void setupCategory() throws Exception {
        Request request = new Request(HttpMethod.GET, "/category/" + categoryId, null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        categoryDto = mapper.readValue(response.getBody(), CategoryDto.class);
        nameLabel.setText(categoryDto.getName());
        descriptionLabel.setText(categoryDto.getDescription());
    }

    @FXML
    private void editCategory() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/categories/CategoryUpdate.fxml"));
        Parent root = fxmlLoader.load();
        CategoryUpdateViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setCategoryId(categoryId);
        controller.setupCategoryUpdate();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }

    @FXML
    private void deleteCategory() throws Exception {
        Request request = new Request(HttpMethod.DELETE, "/category/" + categoryId, null, properties.getAccessToken());
        Response response = properties.getClient().send(request);

        if (response.getCode() == HttpCode.OK) {
            returnBack();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Profile update error");
            alert.setContentText("Profile update failed");
            alert.showAndWait();
        }
    }

    @FXML
    private void viewProducts() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/products/Products.fxml"));
        Parent root = fxmlLoader.load();
        ProductsViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setCategoryId(categoryId);
        controller.setupProducts();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
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
