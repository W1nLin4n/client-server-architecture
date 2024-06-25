package com.w1nlin4n.practice5.services;

import com.w1nlin4n.practice5.database.ProductsDB;
import com.w1nlin4n.practice5.dto.AddProductToCategoryDto;
import com.w1nlin4n.practice5.dto.CategoryDto;
import com.w1nlin4n.practice5.dto.ProductDto;
import com.w1nlin4n.practice5.entities.Category;
import com.w1nlin4n.practice5.entities.Product;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CategoryService {
    private final ProductsDB productsDB;

    public void createCategory(CategoryDto category) {
        productsDB.createCategory(category.toCategory());
    }

    public CategoryDto getCategory(String categoryName) {
        return CategoryDto.fromCategory(productsDB.getCategory(categoryName));
    }

    public void updateCategory(CategoryDto category) {
        productsDB.updateCategory(category.toCategory());
    }

    public void deleteCategory(String categoryName) {
        productsDB.deleteCategory(categoryName);
    }

    public void addProductToCategory(AddProductToCategoryDto addProductToCategoryDto) {
        productsDB.addProductToCategory(addProductToCategoryDto.getProductName(), addProductToCategoryDto.getCategoryName());
    }

    public List<ProductDto> getAllProductsFromCategory(String categoryName) {
        List<ProductDto> result = new ArrayList<>();
        for (Product product : productsDB.getAllProductsFromCategory(categoryName)) {
            result.add(ProductDto.fromProduct(product));
        }
        return result;
    }

    public List<CategoryDto> getAllCategories() {
        List<CategoryDto> result = new ArrayList<>();
        for (Category category : productsDB.getAllCategories()) {
            result.add(CategoryDto.fromCategory(category));
        }
        return result;
    }
}
