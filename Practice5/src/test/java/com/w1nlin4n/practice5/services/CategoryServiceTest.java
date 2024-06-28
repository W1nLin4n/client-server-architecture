package com.w1nlin4n.practice5.services;

import com.w1nlin4n.practice5.database.ProductsDB;
import com.w1nlin4n.practice5.dto.CategoryDto;
import com.w1nlin4n.practice5.dto.ProductDto;
import com.w1nlin4n.practice5.entities.Category;
import com.w1nlin4n.practice5.entities.Product;
import com.w1nlin4n.practice5.exceptions.DatabaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CategoryServiceTest {
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
    void createCategory() {
        CategoryDto categoryDto = CategoryDto
                .builder()
                .name("Automobiles")
                .description("people drive in these")
                .build();
        categoryService.createCategory(categoryDto);
        CategoryDto categoryDtoFromDB = categoryService.getCategoryByName("Automobiles");
        categoryDto.setId(null);
        categoryDtoFromDB.setId(null);
        assertEquals(categoryDto, categoryDtoFromDB);
    }

    @Test
    void getCategory() {
        CategoryDto categoryDto = CategoryDto
                .builder()
                .name("Drinks")
                .description("these are what people drink")
                .build();
        CategoryDto categoryDtoFromDB = categoryService.getCategoryByName("Drinks");
        categoryDto.setId(null);
        categoryDtoFromDB.setId(null);
        assertEquals(categoryDto, categoryDtoFromDB);
    }

    @Test
    void updateCategory() {
        CategoryDto categoryDto = categoryService.getCategoryByName("Drinks");
        categoryDto.setDescription("people drink these");

        categoryService.updateCategory(categoryDto.getId(), categoryDto);

        CategoryDto categoryDtoFromDB = categoryService.getCategoryByName("Drinks");
        categoryDto.setId(null);
        categoryDtoFromDB.setId(null);
        assertEquals(categoryDto, categoryDtoFromDB);
    }

    @Test
    void deleteCategory() {
        categoryService.deleteCategory(categoryService.getCategoryByName("Drinks").getId());
        assertThrows(DatabaseException.class, () -> categoryService.getCategoryByName("Drinks"));
        assertThrows(DatabaseException.class, () -> productService.getProductByName("Wine"));
    }

    @Test
    void getAllProductsFromCategory() {
        List<ProductDto> productDtoList = categoryService.getAllProductsFromCategory(categoryService.getCategoryByName("Food").getId());
        for (ProductDto product : productDtoList) {
            product.setId(null);
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
                                .build()
                ),
                Set.copyOf(productDtoList)
        );
    }

    @Test
    void getAllCategories() {
        List<CategoryDto> categoryDtoList = categoryService.getAllCategories();
        for (CategoryDto categoryDto : categoryDtoList) {
            categoryDto.setId(null);
        }
        assertEquals(
                Set.of(
                        CategoryDto
                                .builder()
                                .id(null)
                                .name("Food")
                                .description("these are what people eat")
                                .build(),
                        CategoryDto
                                .builder()
                                .id(null)
                                .name("Drinks")
                                .description("these are what people drink")
                                .build()
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