package com.w1nlin4n.practice5.controllers;

import com.w1nlin4n.practice5.controllers.endpoint.Endpoint;
import com.w1nlin4n.practice5.networking.HttpCode;
import com.w1nlin4n.practice5.networking.HttpMethod;
import com.w1nlin4n.practice5.controllers.endpoint.params.Body;
import com.w1nlin4n.practice5.controllers.endpoint.params.Path;
import com.w1nlin4n.practice5.controllers.security.AccessLevel;
import com.w1nlin4n.practice5.controllers.security.Security;
import com.w1nlin4n.practice5.dto.CategoryDto;
import com.w1nlin4n.practice5.dto.ProductDto;
import com.w1nlin4n.practice5.services.CategoryService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Controller(service = CategoryService.class)
public class CategoryController {
    private final CategoryService categoryService;

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category", method = HttpMethod.POST, onSuccess = HttpCode.CREATED)
    public void createCategory(@Body CategoryDto category) {
        categoryService.createCategory(category);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category/{id}", method = HttpMethod.GET, onFailure = HttpCode.NOT_FOUND)
    public CategoryDto getCategory(@Path(name = "id") Integer id) {
        return categoryService.getCategory(id);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category/{id}", method = HttpMethod.PUT)
    public void updateCategory(@Path(name = "id") Integer id, @Body CategoryDto category) {
        categoryService.updateCategory(id, category);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category/{id}", method = HttpMethod.DELETE)
    public void deleteCategory(@Path(name = "id") Integer id) {
        categoryService.deleteCategory(id);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category/{id}/products", method = HttpMethod.GET)
    public List<ProductDto> getAllProductsFromCategory(@Path(name = "id") Integer id) {
        return categoryService.getAllProductsFromCategory(id);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category/all", method = HttpMethod.GET)
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

}
