package com.w1nlin4n.homework2.services;

import com.w1nlin4n.homework2.database.ProductsDB;
import com.w1nlin4n.homework2.dto.ProductAmountChangeDto;
import com.w1nlin4n.homework2.dto.ProductDto;
import com.w1nlin4n.homework2.entities.Category;
import com.w1nlin4n.homework2.entities.Product;
import com.w1nlin4n.homework2.exceptions.DatabaseException;
import com.w1nlin4n.homework2.exceptions.LogicException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {
    ProductsDB productsDB;
    ProductService productService;

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
        productService = new ProductService(productsDB);
    }

    @Test
    void createProduct() {
        ProductDto productDto = new ProductDto("Soup", "german food", "Germany", 60, 20.);
        productService.createProduct(productDto);
        assertEquals(productDto, productService.getProduct("Soup"));
        ProductDto productDto2 = new ProductDto("Meat", "general food", "USA", -1, 15.);
        assertThrows(LogicException.class, () -> productService.createProduct(productDto2));
        ProductDto productDto3 = new ProductDto("Milk", "general food", "USA", 10, -.1);
        assertThrows(LogicException.class, () -> productService.createProduct(productDto3));
    }

    @Test
    void getProduct() {
        ProductDto productDto = new ProductDto("Pizza", "italian food", "Italy", 20, 12.5);
        assertEquals(productDto, productService.getProduct("Pizza"));
    }

    @Test
    void updateProduct() {
        ProductDto productDto = new ProductDto("Pizza", "polish food", "Poland", 25, 13.5);
        productService.updateProduct(productDto);
        assertEquals(productDto, productService.getProduct("Pizza"));
        ProductDto productDto2 = new ProductDto("Pizza", "polish food", "Poland", -1, 15.);
        assertThrows(LogicException.class, () -> productService.updateProduct(productDto2));
        ProductDto productDto3 = new ProductDto("Pizza", "polish food", "Poland", 10, -.1);
        assertThrows(LogicException.class, () -> productService.updateProduct(productDto3));
    }

    @Test
    void deleteProduct() {
        productService.deleteProduct("Pizza");
        assertThrows(DatabaseException.class, () -> productService.getProduct("Pizza"));
    }

    @Test
    void addAmountToProduct() {
        ProductAmountChangeDto productAmountChangeDto = new ProductAmountChangeDto("Sushi", 10);
        productService.addAmountToProduct(productAmountChangeDto);
        assertEquals(20, productService.getProduct("Sushi").getAmount());
    }

    @Test
    void removeAmountFromProduct() {
        ProductAmountChangeDto productAmountChangeDto = new ProductAmountChangeDto("Sushi", 10);
        productService.removeAmountFromProduct(productAmountChangeDto);
        assertEquals(0, productService.getProduct("Sushi").getAmount());
        assertThrows(LogicException.class, () -> productService.removeAmountFromProduct(productAmountChangeDto));
    }

    @Test
    void getAllProducts() {
        List<ProductDto> productDtoList = productService.getAllProducts();
        assertEquals(
                Set.of(
                        new ProductDto("Pizza", "italian food", "Italy", 20, 12.5),
                        new ProductDto("Sushi", "japanese food", "Japan", 10, 10.),
                        new ProductDto("Wine", "alcohol drink", "France", 30, 13.),
                        new ProductDto("Water", "regular drink", "Ukraine", 100, 5.)
                ),
                Set.copyOf(productDtoList)
        );
    }

    @AfterEach
    void tearDown() {
        productService = null;
        productsDB = null;
    }
}