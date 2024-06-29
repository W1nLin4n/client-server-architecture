package com.w1nlin4n.project.viewcontrollers.main.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.dto.UserDto;
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

public class UsersViewController {
    private List<UserDto> userDtoList;
    private List<UserDto> filteredUserDtoList;

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

    public void setupUsers() throws Exception {
        Request request = new Request(HttpMethod.GET, "/auth/all", null, properties.getAccessToken());
        Response response = properties.getClient().send(request);
        ObjectMapper mapper = new ObjectMapper();
        userDtoList = Arrays.asList(mapper.readValue(response.getBody(), UserDto[].class));
        filteredUserDtoList = List.copyOf(userDtoList);
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
        filteredUserDtoList = List
                .copyOf(userDtoList)
                .stream()
                .filter(
                        userDto -> userDto.getUsername().toUpperCase().contains(searchField.getText().toUpperCase())
                )
                .toList();
        pagination.setCurrentPageIndex(0);
        renderPage();
    }

    private void updatePage() throws Exception {
        renderPage();
    }

    @FXML
    private void createUser() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/users/UserCreate.fxml"));
        Parent root = fxmlLoader.load();
        UserCreateViewController controller = fxmlLoader.getController();
        controller.setProperties(properties);
        controller.setContentPane(contentPane);
        contentPane.getChildren().clear();
        contentPane.getChildren().add(root);
    }

    private void renderPage() throws Exception {
        int pages = filteredUserDtoList.size() / 5 + (filteredUserDtoList.size() % 5 == 0 ? 0 : 1);
        pagination.setPageCount(pages);
        paginatedList.getChildren().clear();
        int startIndex = pagination.getCurrentPageIndex() * 5;
        for (int i = startIndex; i < Math.min(startIndex + 5, filteredUserDtoList.size()); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main/users/UserItem.fxml"));
            Pane root = fxmlLoader.load();
            UserItemViewController controller = fxmlLoader.getController();
            controller.setProperties(properties);
            controller.setContentPane(contentPane);
            controller.setUserId(filteredUserDtoList.get(i).getId());
            controller.setupUserItem();
            paginatedList.getChildren().add(root);
        }
    }
}
