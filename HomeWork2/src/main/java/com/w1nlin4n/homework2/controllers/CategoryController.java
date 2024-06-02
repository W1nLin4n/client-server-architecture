package com.w1nlin4n.homework2.controllers;

import com.w1nlin4n.homework2.dto.AddProductToCategoryDto;
import com.w1nlin4n.homework2.dto.CategoryDto;
import com.w1nlin4n.homework2.dto.ProductDto;
import com.w1nlin4n.homework2.networking.message.MessageCommand;
import com.w1nlin4n.homework2.services.CategoryService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Controller(service = CategoryService.class)
public class CategoryController {
    private final CategoryService categoryService;

    @Endpoint(command = MessageCommand.CREATE_PRODUCT_GROUP, dto = CategoryDto.class)
    public void createCategory(CategoryDto category) {
        categoryService.createCategory(category);
    }

    @Endpoint(command = MessageCommand.GET_PRODUCT_GROUP, dto = String.class)
    public CategoryDto getCategory(String categoryName) {
        return categoryService.getCategory(categoryName);
    }

    @Endpoint(command = MessageCommand.UPDATE_PRODUCT_GROUP, dto = CategoryDto.class)
    public void updateCategory(CategoryDto category) {
        categoryService.updateCategory(category);
    }

    @Endpoint(command = MessageCommand.DELETE_PRODUCT_GROUP, dto = String.class)
    public void deleteCategory(String categoryName) {
        categoryService.deleteCategory(categoryName);
    }

    @Endpoint(command = MessageCommand.ADD_PRODUCT_TO_GROUP, dto = AddProductToCategoryDto.class)
    public void addProductToCategory(AddProductToCategoryDto addProductToCategoryDto) {
        categoryService.addProductToCategory(addProductToCategoryDto);
    }

    @Endpoint(command = MessageCommand.GET_PRODUCTS_FROM_GROUP, dto = String.class)
    public List<ProductDto> getAllProductsFromCategory(String categoryName) {
        return categoryService.getAllProductsFromCategory(categoryName);
    }

    @Endpoint(command = MessageCommand.GET_ALL_PRODUCT_GROUPS)
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

}
