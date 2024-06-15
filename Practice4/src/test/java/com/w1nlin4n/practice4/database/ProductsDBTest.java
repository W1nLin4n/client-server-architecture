package com.w1nlin4n.practice4.database;

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

class ProductsDBTest {
    ProductsDB productsDB;

    @BeforeEach
    void setUp() {
        productsDB = new ProductsDB();
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
    }

    @Test
    void createCategory() {
        Category category = new Category("Automobiles", "people drive in these");
        productsDB.createCategory(category);
        assertEquals(category, productsDB.getCategory("Automobiles"));
    }

    @Test
    void getCategory() {
        Category category = new Category("Drinks", "these are what people drink");
        assertEquals(category, productsDB.getCategory("Drinks"));
    }

    @Test
    void updateCategory() {
        Category category = new Category("Drinks", "people drink these");
        productsDB.updateCategory(category);
        assertEquals(category, productsDB.getCategory("Drinks"));
    }

    @Test
    void deleteCategory() {
        productsDB.deleteCategory("Drinks");
        assertThrows(DatabaseException.class, () -> productsDB.getCategory("Drinks"));
        assertThrows(DatabaseException.class, () -> productsDB.getProduct("Wine"));
        assertEquals(new ArrayList<Product>(), productsDB.getAllProductsFromCategory("Drinks"));
    }

    @Test
    void getAllCategories() {
        List<Category> categoryList = productsDB.getAllCategories();
        assertEquals(
                Set.of(
                        new Category("Food", "these are what people eat"),
                        new Category("Drinks", "these are what people drink")
                ),
                Set.copyOf(categoryList)
        );
    }

    @Test
    void createProduct() {
        Product product = new Product("Soup", "german food", "Germany", 60, 20.);
        productsDB.createProduct(product);
        assertEquals(product, productsDB.getProduct("Soup"));
    }

    @Test
    void getProduct() {
        Product product = new Product("Pizza", "italian food", "Italy", 20, 12.5);
        assertEquals(product, productsDB.getProduct("Pizza"));
    }

    @Test
    void updateProduct() {
        Product product = new Product("Pizza", "polish food", "Poland", 25, 13.5);
        productsDB.updateProduct(product);
        assertEquals(product, productsDB.getProduct("Pizza"));
    }

    @Test
    void deleteProduct() {
        productsDB.deleteProduct("Pizza");
        assertThrows(DatabaseException.class, () -> productsDB.getProduct("Pizza"));
    }

    @Test
    void getAllProducts() {
        List<Product> productList = productsDB.getAllProducts();
        assertEquals(
                Set.of(
                        new Product("Pizza", "italian food", "Italy", 20, 12.5),
                        new Product("Sushi", "japanese food", "Japan", 10, 10.),
                        new Product("Wine", "alcohol drink", "France", 30, 13.),
                        new Product("Water", "regular drink", "Ukraine", 100, 5.)
                ),
                Set.copyOf(productList)
        );
    }

    @Test
    void addProductToCategory() {
        Product product = new Product("Soup", "german food", "Germany", 60, 20.);
        productsDB.createProduct(product);
        productsDB.addProductToCategory("Soup", "Food");
        assertTrue(() -> productsDB.getAllProducts().contains(product));
    }

    @Test
    void getAllProductsFromCategory() {
        List<Product> productList = productsDB.getAllProductsFromCategory("Food");
        assertEquals(
                Set.of(
                        new Product("Pizza", "italian food", "Italy", 20, 12.5),
                        new Product("Sushi", "japanese food", "Japan", 10, 10.)
                ),
                Set.copyOf(productList)
        );
    }

    @AfterEach
    void tearDown() {
        productsDB = null;
    }
}