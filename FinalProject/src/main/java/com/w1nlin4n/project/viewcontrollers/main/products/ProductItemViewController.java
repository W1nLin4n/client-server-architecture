package com.w1nlin4n.project.viewcontrollers.main.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.dto.CategoryDto;
import com.w1nlin4n.project.dto.ProductDto;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.networking.message.Request;
import com.w1nlin4n.project.networking.message.Response;
import com.w1nlin4n.project.viewcontrollers.properties.Properties;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class ProductItemViewController {
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
    private HBox categoryLabelContainer;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label totalPriceLabel;

    public void setupProductItem() throws Exception {
        Request request = new Request(HttpMethod.GET, "/product/" + productId, null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        productDto = mapper.readValue(response.getBody(), ProductDto.class);
        nameLabel.setText(productDto.getName());
        if (categoryId == null) {
            Request categoryRequest = new Request(HttpMethod.GET, "/category/" + productDto.getCategoryId(), null, properties.getAccessToken());
            Response categoryResponse = properties.getClient().send(categoryRequest);
            CategoryDto categoryDto = mapper.readValue(categoryResponse.getBody(), CategoryDto.class);
            categoryLabel.setText(categoryDto.getName());
        } else {
            categoryLabelContainer.setManaged(false);
            categoryLabelContainer.setVisible(false);
        }
        totalPriceLabel.setText(String.format("%.2f", productDto.getPrice() * productDto.getAmount()));
    }

    @FXML
    private void viewProduct() throws Exception {
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
