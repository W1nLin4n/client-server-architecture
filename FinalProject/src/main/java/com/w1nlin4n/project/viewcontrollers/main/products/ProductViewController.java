package com.w1nlin4n.project.viewcontrollers.main.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.dto.CategoryDto;
import com.w1nlin4n.project.dto.ProductDto;
import com.w1nlin4n.project.networking.HttpCode;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.networking.message.Request;
import com.w1nlin4n.project.networking.message.Response;
import com.w1nlin4n.project.utility.Validator;
import com.w1nlin4n.project.viewcontrollers.properties.Properties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class ProductViewController {
    private ProductDto productDto;

    @Setter
    private int productId;

    @Setter
    private Integer categoryId;

    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label manufacturerLabel;

    @FXML
    private Label amountLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private TextField changeAmountField;

    public void setupProduct() throws Exception {
        Request request = new Request(HttpMethod.GET, "/product/" + productId, null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        productDto = mapper.readValue(response.getBody(), ProductDto.class);
        nameLabel.setText(productDto.getName());
        descriptionLabel.setText(productDto.getDescription());
        manufacturerLabel.setText(productDto.getManufacturer());
        amountLabel.setText(String.valueOf(productDto.getAmount()));
        priceLabel.setText(String.valueOf(productDto.getPrice()));

        Request categoryRequest = new Request(HttpMethod.GET, "/category/" + productDto.getCategoryId(), null, properties.getAccessToken());
        Response categoryResponse = properties.getClient().send(categoryRequest);
        CategoryDto categoryDto = mapper.readValue(categoryResponse.getBody(), CategoryDto.class);
        categoryLabel.setText(categoryDto.getName());
    }

    @FXML
    private void addAmount() throws Exception {
        if (!Validator.validateInteger(changeAmountField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product update error");
            alert.setContentText("Product amount change value must be a valid integer");
            alert.showAndWait();
            return;
        }
        Request request = new Request(HttpMethod.PUT, "/product/" + productId + "/add/" + changeAmountField.getText(), null, properties.getAccessToken());
        Response response = properties.getClient().send(request);

        if (response.getCode() == HttpCode.OK) {
            setupProduct();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product update error");
            alert.setContentText("Could not add amount to product");
            alert.showAndWait();
        }
    }

    @FXML
    private void removeAmount() throws Exception {
        if (!Validator.validateInteger(changeAmountField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product update error");
            alert.setContentText("Product amount change value must be a valid integer");
            alert.showAndWait();
            return;
        }
        Request request = new Request(HttpMethod.PUT, "/product/" + productId + "/remove/" + changeAmountField.getText(), null, properties.getAccessToken());
        Response response = properties.getClient().send(request);

        if (response.getCode() == HttpCode.OK) {
            setupProduct();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product update error");
            alert.setContentText("Could not remove amount from product");
            alert.showAndWait();
        }
    }

    @FXML
    private void editProduct() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/products/ProductUpdate.fxml"));
        Parent root = fxmlLoader.load();
        ProductUpdateViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setProductId(productId);
        controller.setCategoryId(categoryId);
        controller.setupProductUpdate();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }

    @FXML
    private void deleteProduct() throws Exception {
        Request request = new Request(HttpMethod.DELETE, "/product/" + productId, null, properties.getAccessToken());
        Response response = properties.getClient().send(request);

        if (response.getCode() == HttpCode.OK) {
            returnBack();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product update error");
            alert.setContentText("Product update failed");
            alert.showAndWait();
        }
    }

    @FXML
    private void returnBack() throws Exception {
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
}
