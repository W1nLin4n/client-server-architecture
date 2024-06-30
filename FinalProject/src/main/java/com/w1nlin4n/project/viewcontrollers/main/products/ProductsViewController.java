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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

public class ProductsViewController {
    private List<ProductDto> productDtoList;
    private List<ProductDto> filteredProductDtoList;

    @Setter
    private Integer categoryId;

    @Setter
    private Properties properties;

    @Setter
    private Pane contentPane;

    @FXML
    private TextField searchField;

    @FXML
    private Pagination pagination;

    @FXML
    private VBox paginatedList;

    @FXML
    private Button addProductButton;

    @FXML
    private HBox categoryLabelContainer;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label totalPriceLabel;

    public void setupProducts() throws Exception {
        Request request;
        if (categoryId == null) {
            addProductButton.setManaged(false);
            addProductButton.setVisible(false);
            categoryLabelContainer.setManaged(false);
            categoryLabelContainer.setVisible(false);
            request = new Request(HttpMethod.GET, "/product/all", null, properties.getAccessToken());
        } else {
            Request categoryRequest = new Request(HttpMethod.GET, "/category/" + categoryId, null, properties.getAccessToken());
            Response categoryResponse = properties.getClient().send(categoryRequest);
            ObjectMapper mapper = new ObjectMapper();
            CategoryDto categoryDto = mapper.readValue(categoryResponse.getBody(), CategoryDto.class);
            categoryLabel.setText(categoryDto.getName());
            request = new Request(HttpMethod.GET, "/category/" + categoryId + "/products", null, properties.getAccessToken());
        }
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        productDtoList = Arrays.asList(mapper.readValue(response.getBody(), ProductDto[].class));
        filteredProductDtoList = List.copyOf(productDtoList);
        System.out.println(filteredProductDtoList);
        renderPage();
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            try {
                updatePage();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                updateSearch();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateSearch() throws Exception {
        filteredProductDtoList = List
                .copyOf(productDtoList)
                .stream()
                .filter(
                        userDto -> userDto.getName().toUpperCase().contains(searchField.getText().toUpperCase())
                )
                .toList();
        pagination.setCurrentPageIndex(0);
        renderPage();
    }

    private void updatePage() throws Exception {
        renderPage();
    }

    @FXML
    private void createProduct() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/products/ProductCreate.fxml"));
        Parent root = fxmlLoader.load();
        ProductCreateViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        controller.setCategoryId(categoryId);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }

    private void renderPage() throws Exception {
        int pages = filteredProductDtoList.size() / 5 + (filteredProductDtoList.size() % 5 == 0 ? 0 : 1);
        pagination.setPageCount(pages);
        paginatedList.getChildren().clear();
        Double totalPrice = 0.;
        int startIndex = pagination.getCurrentPageIndex() * 5;
        for (int i = startIndex; i < Math.min(startIndex + 5, filteredProductDtoList.size()); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/products/ProductItem.fxml"));
            Pane root = fxmlLoader.load();
            ProductItemViewController controller = fxmlLoader.getController();
            controller.setProperties(properties);
            controller.setContentPane(contentPane);
            controller.setProductId(productDtoList.get(i).getId());
            controller.setCategoryId(categoryId);
            controller.setupProductItem();
            paginatedList.getChildren().add(root);
            totalPrice += filteredProductDtoList.get(i).getPrice() * filteredProductDtoList.get(i).getAmount();
        }
        totalPriceLabel.setText(String.format("%.2f", totalPrice));
    }
}
