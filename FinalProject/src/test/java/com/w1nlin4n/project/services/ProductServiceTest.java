package com.w1nlin4n.project.services;

import com.w1nlin4n.project.database.ProductsDB;
import com.w1nlin4n.project.dto.ProductDto;
import com.w1nlin4n.project.entities.Category;
import com.w1nlin4n.project.entities.Product;
import com.w1nlin4n.project.exceptions.DatabaseException;
import com.w1nlin4n.project.exceptions.LogicException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTest {
    ProductsDB productsDB;
    CategoryService categoryService;
    ProductService productService;

    @BeforeEach
    void setUp() {
        productsDB = new ProductsDB("jdbc:sqlite::memory:");
        Category category1 = Category
                .builder()
                .name("Food")
                .description("these are what people eat")
                .build();
        Category category2 = Category
                .builder()
                .name("Drinks")
                .description("these are what people drink")
                .build();
        Product product1 = Product
                .builder()
                .name("Pizza")
                .description("italian food")
                .manufacturer("Italy")
                .amount(20)
                .price(12.5)
                .build();
        Product product2 = Product
                .builder()
                .name("Sushi")
                .description("japanese food")
                .manufacturer("Japan")
                .amount(10)
                .price(10.)
                .build();
        Product product3 = Product
                .builder()
                .name("Whine")
                .description("alcohol drink")
                .manufacturer("France")
                .amount(30)
                .price(13.)
                .build();
        Product product4 = Product
                .builder()
                .name("Water")
                .description("regular drink")
                .manufacturer("Ukraine")
                .amount(100)
                .price(5.)
                .build();
        productsDB.createCategory(category1);
        productsDB.createCategory(category2);
        Integer category1Id = productsDB.getCategoryByName("Food").getId();
        Integer category2Id = productsDB.getCategoryByName("Drinks").getId();
        product1.setCategoryId(category1Id);
        product2.setCategoryId(category1Id);
        product3.setCategoryId(category2Id);
        product4.setCategoryId(category2Id);
        productsDB.createProduct(product1);
        productsDB.createProduct(product2);
        productsDB.createProduct(product3);
        productsDB.createProduct(product4);
        categoryService = new CategoryService(productsDB);
        productService = new ProductService(productsDB);
    }

    @Test
    void createProduct() {
        ProductDto productDto = ProductDto
                .builder()
                .categoryId(categoryService.getCategoryByName("Food").getId())
                .name("Soup")
                .description("german food")
                .manufacturer("Germany")
                .amount(60)
                .price(20.)
                .build();
        productService.createProduct(productDto);
        ProductDto productDtoFromDB = productService.getProductByName("Soup");
        productDto.setId(null);
        productDtoFromDB.setId(null);
        assertEquals(productDto, productDtoFromDB);
        ProductDto productDto2 = ProductDto
                .builder()
                .categoryId(categoryService.getCategoryByName("Food").getId())
                .name("Meat")
                .description("general food")
                .manufacturer("USA")
                .amount(-1)
                .price(15.)
                .build();
        assertThrows(LogicException.class, () -> productService.createProduct(productDto2));
        ProductDto productDto3 = ProductDto
                .builder()
                .categoryId(categoryService.getCategoryByName("Food").getId())
                .name("Milk")
                .description("general food")
                .manufacturer("USA")
                .amount(10)
                .price(-.1)
                .build();
        assertThrows(LogicException.class, () -> productService.createProduct(productDto3));
    }

    @Test
    void getProduct() {
        ProductDto productDto = ProductDto
                .builder()
                .categoryId(categoryService.getCategoryByName("Food").getId())
                .name("Pizza")
                .description("italian food")
                .manufacturer("Italy")
                .amount(20)
                .price(12.5)
                .build();
        ProductDto productDtoFromDB = productService.getProductByName("Pizza");
        productDto.setId(null);
        productDtoFromDB.setId(null);
        assertEquals(productDto, productDtoFromDB);
    }

    @Test
    void updateProduct() {
        ProductDto productDto = ProductDto
                .builder()
                .categoryId(categoryService.getCategoryByName("Food").getId())
                .name("Pizza")
                .description("polish food")
                .manufacturer("Poland")
                .amount(25)
                .price(13.5)
                .build();
        productService.updateProduct(productService.getProductByName("Pizza").getId(), productDto);
        ProductDto productDtoFromDB = productService.getProductByName("Pizza");
        productDto.setId(null);
        productDtoFromDB.setId(null);
        assertEquals(productDto, productDtoFromDB);
        ProductDto productDto2 = ProductDto
                .builder()
                .categoryId(categoryService.getCategoryByName("Food").getId())
                .name("Pizza")
                .description("polish food")
                .manufacturer("Poland")
                .amount(-1)
                .price(15.)
                .build();
        assertThrows(LogicException.class, () -> productService.updateProduct(productService.getProductByName("Pizza").getId(), productDto2));
        ProductDto productDto3 = ProductDto
                .builder()
                .categoryId(categoryService.getCategoryByName("Food").getId())
                .name("Pizza")
                .description("polish food")
                .manufacturer("Poland")
                .amount(1)
                .price(-.1)
                .build();
        assertThrows(LogicException.class, () -> productService.updateProduct(productService.getProductByName("Pizza").getId(), productDto3));
    }

    @Test
    void deleteProduct() {
        productService.deleteProduct(productService.getProductByName("Pizza").getId());
        assertThrows(DatabaseException.class, () -> productService.getProductByName("Pizza"));
    }

    @Test
    void addAmountToProduct() {
        productService.addAmountToProduct(productService.getProductByName("Sushi").getId(), 10);
        Assertions.assertEquals(20, productService.getProductByName("Sushi").getAmount());
    }

    @Test
    void removeAmountFromProduct() {
        productService.removeAmountFromProduct(productService.getProductByName("Sushi").getId(), 10);
        Assertions.assertEquals(0, productService.getProductByName("Sushi").getAmount());
        assertThrows(LogicException.class, () -> productService.removeAmountFromProduct(productService.getProductByName("Sushi").getId(), 10));
    }

    @Test
    void getAllProducts() {
        List<ProductDto> productDtoList = productService.getAllProducts();
        for (ProductDto productDto : productDtoList) {
            productDto.setId(null);
        }
        assertEquals(
                Set.of(
                        ProductDto
                                .builder()
                                .id(null)
                                .categoryId(categoryService.getCategoryByName("Food").getId())
                                .name("Pizza")
                                .description("italian food")
                                .manufacturer("Italy")
                                .amount(20)
                                .price(12.5)
                                .build(),
                        ProductDto
                                .builder()
                                .id(null)
                                .categoryId(categoryService.getCategoryByName("Food").getId())
                                .name("Sushi")
                                .description("japanese food")
                                .manufacturer("Japan")
                                .amount(10)
                                .price(10.)
                                .build(),
                        ProductDto
                                .builder()
                                .id(null)
                                .categoryId(categoryService.getCategoryByName("Drinks").getId())
                                .name("Whine")
                                .description("alcohol drink")
                                .manufacturer("France")
                                .amount(30)
                                .price(13.)
                                .build(),
                        ProductDto
                                .builder()
                                .id(null)
                                .categoryId(categoryService.getCategoryByName("Drinks").getId())
                                .name("Water")
                                .description("regular drink")
                                .manufacturer("Ukraine")
                                .amount(100)
                                .price(5.)
                                .build()
                ),
                Set.copyOf(productDtoList)
        );
    }

    @AfterEach
    void tearDown() {
        categoryService = null;
        productService = null;
        productsDB = null;
    }
}