package com.w1nlin4n.practice4.services;

import com.w1nlin4n.practice4.database.ProductsDB;
import com.w1nlin4n.practice4.dto.AddProductToCategoryDto;
import com.w1nlin4n.practice4.dto.CategoryDto;
import com.w1nlin4n.practice4.dto.ProductDto;
import com.w1nlin4n.practice4.entities.Category;
import com.w1nlin4n.practice4.entities.Product;
import com.w1nlin4n.practice4.exceptions.DatabaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceTest {
    ProductsDB productsDB;
    CategoryService categoryService;
    ProductService productService;

    @BeforeEach
    void setUp() {
        productsDB = new ProductsDB("jdbc:sqlite::memory:");
        Category category1 = new Category("Food", "these are what people eat");
        Category category2 = new Category("Drinks", "these are what people drink");
        Product product1 = new Product("Pizza", "italian food", "Italy", 20, 12.5);
        Product product2 = new Product("Sushi", "japanese food", "Japan", 10, 10.);
        Product product3 = new Product("Wine", "alcohol drink", "France", 30, 13.);
        Product product4 = new Product("Water", "regular drink", "Ukraine", 100, 5.);
        productsDB.createCategory(category1);
        productsDB.createCategory(category2);
        productsDB.createProduct(product1);
        productsDB.createProduct(product2);
        productsDB.createProduct(product3);
        productsDB.createProduct(product4);
        productsDB.addProductToCategory("Pizza", "Food");
        productsDB.addProductToCategory("Sushi", "Food");
        productsDB.addProductToCategory("Wine", "Drinks");
        productsDB.addProductToCategory("Water", "Drinks");
        categoryService = new CategoryService(productsDB);
        productService = new ProductService(productsDB);
    }

    @Test
    void createCategory() {
        CategoryDto categoryDto = new CategoryDto("Automobiles", "people drive in these");
        categoryService.createCategory(categoryDto);
        assertEquals(categoryDto, categoryService.getCategory("Automobiles"));
    }

    @Test
    void getCategory() {
        CategoryDto categoryDto = new CategoryDto("Drinks", "these are what people drink");
        assertEquals(categoryDto, categoryService.getCategory("Drinks"));
    }

    @Test
    void updateCategory() {
        CategoryDto categoryDto = new CategoryDto("Drinks", "people drink these");
        categoryService.updateCategory(categoryDto);
        assertEquals(categoryDto, categoryService.getCategory("Drinks"));
    }

    @Test
    void deleteCategory() {
        categoryService.deleteCategory("Drinks");
        assertThrows(DatabaseException.class, () -> categoryService.getCategory("Drinks"));
        assertThrows(DatabaseException.class, () -> productService.getProduct("Wine"));
        assertEquals(new ArrayList<ProductDto>(), categoryService.getAllProductsFromCategory("Drinks"));
    }

    @Test
    void addProductToCategory() {
        ProductDto productDto = new ProductDto("Soup", "german food", "Germany", 60, 20.);
        productService.createProduct(productDto);
        AddProductToCategoryDto addProductToCategoryDto = new AddProductToCategoryDto("Soup", "Food");
        categoryService.addProductToCategory(addProductToCategoryDto);
        assertTrue(() -> categoryService.getAllProductsFromCategory("Food").contains(productDto));
    }

    @Test
    void getAllProductsFromCategory() {
        List<ProductDto> productDtoList = categoryService.getAllProductsFromCategory("Food");
        assertEquals(
                Set.of(
                        new ProductDto("Pizza", "italian food", "Italy", 20, 12.5),
                        new ProductDto("Sushi", "japanese food", "Japan", 10, 10.)
                ),
                Set.copyOf(productDtoList)
        );
    }

    @Test
    void getAllCategories() {
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();
        assertEquals(
                Set.of(
                        new CategoryDto("Food", "these are what people eat"),
                        new CategoryDto("Drinks", "these are what people drink")
                ),
                Set.copyOf(categoryDtoList)
        );
    }

    @AfterEach
    void tearDown() {
        categoryService = null;
        productService = null;
        productsDB = null;
    }
}