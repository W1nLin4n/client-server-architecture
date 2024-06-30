package com.w1nlin4n.project.database;

import com.w1nlin4n.project.controllers.security.AccessLevel;
import com.w1nlin4n.project.entities.Category;
import com.w1nlin4n.project.entities.Product;
import com.w1nlin4n.project.entities.User;
import com.w1nlin4n.project.exceptions.DatabaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductsDBTest {
    ProductsDB productsDB;

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
                .name("Wine")
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
    }

    @Test
    void createUser() {
        User user = User
                .builder()
                .username("user")
                .passwordHash("example_hash")
                .accessLevel(AccessLevel.USER.name())
                .build();
        productsDB.createUser(user);
        User userFromDB = productsDB.getUserByUsername("user");
        user.setId(null);
        userFromDB.setId(null);
        assertEquals(user, userFromDB);
    }

    @Test
    void getUser() {
        User user = User
                .builder()
                .username("admin")
                .passwordHash("FB001DFCFFD1C899F3297871406242F097AECF1A5342CCF3EBCD116146188E4B")
                .accessLevel(AccessLevel.ADMIN.name())
                .build();
        User userFromDB = productsDB.getUser(productsDB.getUserByUsername("admin").getId());
        user.setId(null);
        userFromDB.setId(null);
        assertEquals(user, userFromDB);
    }

    @Test
    void updateUser() {
        User user = User
                .builder()
                .username("admin")
                .passwordHash("admin_hash")
                .accessLevel(AccessLevel.ADMIN.name())
                .build();
        productsDB.updateUser(productsDB.getUserByUsername("admin").getId(), user);
        User userFromDB = productsDB.getUserByUsername("admin");
        user.setId(null);
        userFromDB.setId(null);
        assertEquals(user, userFromDB);
    }

    @Test
    void deleteUser() {
        productsDB.deleteUser(productsDB.getUserByUsername("admin").getId());
        assertThrows(DatabaseException.class, () -> productsDB.getUserByUsername("admin"));
    }

    @Test
    void getAllUsers() {
        List<User> userList = productsDB.getAllUsers();
        for (User user : userList) {
            user.setId(null);
        }
        assertEquals(
                Set.of(
                        User
                                .builder()
                                .id(null)
                                .username("admin")
                                .passwordHash("FB001DFCFFD1C899F3297871406242F097AECF1A5342CCF3EBCD116146188E4B")
                                .accessLevel(AccessLevel.ADMIN.name())
                                .build()
                ),
                Set.copyOf(userList)
        );
    }

    @Test
    void createCategory() {
        Category category = Category
                .builder()
                .name("Automobiles")
                .description("people drive in these")
                .build();
        productsDB.createCategory(category);
        Category categoryFromDB = productsDB.getCategoryByName("Automobiles");
        category.setId(null);
        categoryFromDB.setId(null);
        assertEquals(category, categoryFromDB);
    }

    @Test
    void getCategory() {
        Category category = Category
                .builder()
                .name("Drinks")
                .description("these are what people drink")
                .build();
        Category categoryFromDB = productsDB.getCategoryByName("Drinks");
        category.setId(null);
        categoryFromDB.setId(null);
        assertEquals(category, categoryFromDB);
    }

    @Test
    void updateCategory() {
        Category category = productsDB.getCategoryByName("Drinks");
        category.setDescription("people drink these");

        productsDB.updateCategory(category.getId(), category);

        Category categoryFromDB = productsDB.getCategoryByName("Drinks");
        category.setId(null);
        categoryFromDB.setId(null);
        assertEquals(category, categoryFromDB);
    }

    @Test
    void deleteCategory() {
        productsDB.deleteCategory(productsDB.getCategoryByName("Drinks").getId());
        assertThrows(DatabaseException.class, () -> productsDB.getCategoryByName("Drinks"));
        assertThrows(DatabaseException.class, () -> productsDB.getProductByName("Wine"));
    }

    @Test
    void getAllCategories() {
        List<Category> categoryList = productsDB.getAllCategories();
        for (Category category : categoryList) {
            category.setId(null);
        }
        assertEquals(
                Set.of(
                        Category
                                .builder()
                                .id(null)
                                .name("Food")
                                .description("these are what people eat")
                                .build(),
                        Category
                                .builder()
                                .id(null)
                                .name("Drinks")
                                .description("these are what people drink")
                                .build()
                ),
                Set.copyOf(categoryList)
        );
    }

    @Test
    void createProduct() {
        Product product = Product
                .builder()
                .categoryId(productsDB.getCategoryByName("Food").getId())
                .name("Soup")
                .description("german food")
                .manufacturer("Germany")
                .amount(60)
                .price(20.)
                .build();
        productsDB.createProduct(product);
        Product productFromDB = productsDB.getProductByName("Soup");
        product.setId(null);
        productFromDB.setId(null);
        assertEquals(product, productFromDB);
    }

    @Test
    void getProduct() {
        Product product = Product
                .builder()
                .categoryId(productsDB.getCategoryByName("Food").getId())
                .name("Pizza")
                .description("italian food")
                .manufacturer("Italy")
                .amount(20)
                .price(12.5)
                .build();
        Product productFromDB = productsDB.getProductByName("Pizza");
        product.setId(null);
        productFromDB.setId(null);
        assertEquals(product, productFromDB);
    }

    @Test
    void updateProduct() {
        Product product = Product
                .builder()
                .categoryId(productsDB.getCategoryByName("Food").getId())
                .name("Pizza")
                .description("polish food")
                .manufacturer("Poland")
                .amount(25)
                .price(13.5)
                .build();
        productsDB.updateProduct(productsDB.getProductByName("Pizza").getId(), product);
        Product productFromDB = productsDB.getProductByName("Pizza");
        product.setId(null);
        productFromDB.setId(null);
        assertEquals(product, productFromDB);
    }

    @Test
    void deleteProduct() {
        productsDB.deleteProduct(productsDB.getProductByName("Pizza").getId());
        assertThrows(DatabaseException.class, () -> productsDB.getProductByName("Pizza"));
    }

    @Test
    void getAllProducts() {
        List<Product> productList = productsDB.getAllProducts();
        for (Product product : productList) {
            product.setId(null);
        }
        assertEquals(
                Set.of(
                        Product
                                .builder()
                                .id(null)
                                .categoryId(productsDB.getCategoryByName("Food").getId())
                                .name("Pizza")
                                .description("italian food")
                                .manufacturer("Italy")
                                .amount(20)
                                .price(12.5)
                                .build(),
                        Product
                                .builder()
                                .id(null)
                                .categoryId(productsDB.getCategoryByName("Food").getId())
                                .name("Sushi")
                                .description("japanese food")
                                .manufacturer("Japan")
                                .amount(10)
                                .price(10.)
                                .build(),
                        Product
                                .builder()
                                .id(null)
                                .categoryId(productsDB.getCategoryByName("Drinks").getId())
                                .name("Wine")
                                .description("alcohol drink")
                                .manufacturer("France")
                                .amount(30)
                                .price(13.)
                                .build(),
                        Product
                                .builder()
                                .id(null)
                                .categoryId(productsDB.getCategoryByName("Drinks").getId())
                                .name("Water")
                                .description("regular drink")
                                .manufacturer("Ukraine")
                                .amount(100)
                                .price(5.)
                                .build()
                ),
                Set.copyOf(productList)
        );
    }

    @Test
    void getAllProductsFromCategory() {
        List<Product> productList = productsDB.getAllProductsFromCategory(productsDB.getCategoryByName("Food").getId());
        for (Product product : productList) {
            product.setId(null);
        }
        assertEquals(
                Set.of(
                        Product
                                .builder()
                                .id(null)
                                .categoryId(productsDB.getCategoryByName("Food").getId())
                                .name("Pizza")
                                .description("italian food")
                                .manufacturer("Italy")
                                .amount(20)
                                .price(12.5)
                                .build(),
                        Product
                                .builder()
                                .id(null)
                                .categoryId(productsDB.getCategoryByName("Food").getId())
                                .name("Sushi")
                                .description("japanese food")
                                .manufacturer("Japan")
                                .amount(10)
                                .price(10.)
                                .build()
                ),
                Set.copyOf(productList)
        );
    }

    @AfterEach
    void tearDown() {
        productsDB = null;
    }
}