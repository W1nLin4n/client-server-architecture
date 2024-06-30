package com.w1nlin4n.project.viewcontrollers.main.products;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class ProductCreateViewController {
    @Setter
    private Integer categoryId;

    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField manufacturerField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField priceField;

    @FXML
    private void createProduct() throws Exception {
        if (!Validator.validateInteger(amountField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product creation error");
            alert.setContentText("Product amount must be a valid integer");
            alert.showAndWait();
            return;
        }
        if (!Validator.validateDouble(priceField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product creation error");
            alert.setContentText("Product price must be a valid number");
            alert.showAndWait();
            return;
        }
        ProductDto productDto = ProductDto
                .builder()
                .name(nameField.getText())
                .description(descriptionField.getText())
                .manufacturer(manufacturerField.getText())
                .amount(Integer.parseInt(amountField.getText()))
                .price(Double.parseDouble(priceField.getText()))
                .categoryId(categoryId)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(productDto);
        Request request = new Request(HttpMethod.POST, "/product", body, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        if (response.getCode() == HttpCode.CREATED) {
            returnBack();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product creation error");
            alert.setContentText("Couldn't create such product");
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
