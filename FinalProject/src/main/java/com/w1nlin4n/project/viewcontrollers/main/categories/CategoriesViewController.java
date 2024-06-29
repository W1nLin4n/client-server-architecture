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
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

public class CategoriesViewController {
    private List<CategoryDto> categoryDtoList;
    private List<CategoryDto> filteredCategooryDtoList;

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

    public void setupCategories() throws Exception {
        Request request = new Request(HttpMethod.GET, "/category/all", null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        categoryDtoList = Arrays.asList(mapper.readValue(response.getBody(), CategoryDto[].class));
        filteredCategooryDtoList = List.copyOf(categoryDtoList);
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
        filteredCategooryDtoList = List
                .copyOf(categoryDtoList)
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
    private void createCategory() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/categories/CategoryCreate.fxml"));
        Parent root = fxmlLoader.load();
        CategoryCreateViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }

    private void renderPage() throws Exception {
        int pages = filteredCategooryDtoList.size() / 5 + (filteredCategooryDtoList.size() % 5 == 0 ? 0 : 1);
        pagination.setPageCount(pages);
        paginatedList.getChildren().clear();
        int startIndex = pagination.getCurrentPageIndex() * 5;
        for (int i = startIndex; i < Math.min(startIndex + 5, filteredCategooryDtoList.size()); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/categories/CategoryItem.fxml"));
            Pane root = fxmlLoader.load();
            CategoryItemViewController controller = fxmlLoader.getController();
            controller.setProperties(properties);
            controller.setContentPane(contentPane);
            controller.setCategoryId(filteredCategooryDtoList.get(i).getId());
            controller.setupCategoryItem();
            paginatedList.getChildren().add(root);
        }
    }
}
