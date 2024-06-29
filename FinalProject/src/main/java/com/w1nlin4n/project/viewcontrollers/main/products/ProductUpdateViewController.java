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

public class ProductUpdateViewController {
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
    private TextField nameField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField manufacturerField;

    @FXML
    private TextField amountField;

    @FXML
    private TextField priceField;

    public void setupProductUpdate() throws Exception {
        Request request = new Request(HttpMethod.GET, "/product/" + productId, null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        productDto = mapper.readValue(response.getBody(), ProductDto.class);
        nameField.setText(productDto.getName());
        descriptionField.setText(productDto.getDescription());
        manufacturerField.setText(productDto.getManufacturer());
        amountField.setText(String.valueOf(productDto.getAmount()));
        priceField.setText(String.valueOf(productDto.getPrice()));
    }

    @FXML
    private void applyChanges() throws Exception {
        if (!Validator.validateInteger(amountField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product update error");
            alert.setContentText("Product amount must be a valid integer");
            alert.showAndWait();
            return;
        }
        if (!Validator.validateDouble(priceField.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR");
            alert.setHeaderText("Product update error");
            alert.setContentText("Product price must be a valid number");
            alert.showAndWait();
            return;
        }
        ProductDto requestProductDto = ProductDto
                .builder()
                .name(nameField.getText())
                .description(descriptionField.getText())
                .manufacturer(manufacturerField.getText())
                .amount(Integer.parseInt(amountField.getText()))
                .price(Double.parseDouble(priceField.getText()))
                .categoryId(productDto.getCategoryId())
                .build();
        ObjectMapper mapper = new ObjectMapper();
        Request request = new Request(HttpMethod.PUT, "/product/" + productId, mapper.writeValueAsString(requestProductDto), properties.getAccessToken());
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/products/Product.fxml"));
        Parent root = fxmlLoader.load();
        ProductViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setProductId(productId);
        controller.setCategoryId(categoryId);
        controller.setupProduct();
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }
}
