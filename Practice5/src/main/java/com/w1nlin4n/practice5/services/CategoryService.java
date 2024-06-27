package com.w1nlin4n.practice5.services;

import com.w1nlin4n.practice5.database.ProductsDB;
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

    public CategoryDto getCategory(Integer id) {
        return CategoryDto.fromCategory(productsDB.getCategory(id));
    }

    public CategoryDto getCategoryByName(String name) {
        return CategoryDto.fromCategory(productsDB.getCategoryByName(name));
    }

    public void updateCategory(Integer id, CategoryDto category) {
        productsDB.updateCategory(id, category.toCategory());
    }

    public void deleteCategory(Integer id) {
        productsDB.deleteCategory(id);
    }

    public List<ProductDto> getAllProductsFromCategory(Integer id) {
        List<ProductDto> result = new ArrayList<>();
        for (Product product : productsDB.getAllProductsFromCategory(id)) {
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
