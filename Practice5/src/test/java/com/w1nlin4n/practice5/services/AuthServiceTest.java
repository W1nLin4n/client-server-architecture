package com.w1nlin4n.practice5.services;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.w1nlin4n.practice5.controllers.security.AccessLevel;
import com.w1nlin4n.practice5.database.ProductsDB;
import com.w1nlin4n.practice5.dto.LoginDto;
import com.w1nlin4n.practice5.dto.UserDto;
import com.w1nlin4n.practice5.entities.Category;
import com.w1nlin4n.practice5.entities.Product;
import com.w1nlin4n.practice5.exceptions.AuthenticationException;
import com.w1nlin4n.practice5.exceptions.DatabaseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthServiceTest {
    ProductsDB productsDB;
    AuthService authService;
    Algorithm algorithm;

    @BeforeEach
    void setUp() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
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
                .amount(1000)
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
        authService = new AuthService(productsDB);
        UserDto userDto = UserDto
                .builder()
                .username("user")
                .passwordHash("8AC76453D769D4FD14B3F41AD4933F9BD64321972CD002DE9B847E117435B08B")
                .accessLevel(AccessLevel.USER.name())
                .build();
        authService.createUser(userDto);
        algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
    }

    @Test
    void me() throws JsonProcessingException {
        UserDto userDto = authService.getUserByUsername("user");
        LoginDto loginDto = LoginDto
                .builder()
                .username("user")
                .passwordHash("8AC76453D769D4FD14B3F41AD4933F9BD64321972CD002DE9B847E117435B08B")
                .build();
        String accessToken = authService.login(loginDto);
        assertEquals(userDto, authService.me(accessToken));
    }

    @Test
    void login() {
        LoginDto loginDto = LoginDto
                .builder()
                .username("user")
                .passwordHash("8AC76453D769D4FD14B3F41AD4933F9BD64321972CD002DE9B847E117435B08B")
                .build();
        String accessToken = authService.login(loginDto);
        UserDto userDto = authService.getUserByUsername("user");
        UserDto userDtoFromToken = authService.me(accessToken);
        assertEquals(userDto, userDtoFromToken);
        LoginDto loginDto2 = LoginDto
                .builder()
                .username("no_such_user")
                .passwordHash("8AC76453D769D4FD14B3F41AD4933F9BD64321972CD002DE9B847E117435B08B")
                .build();
        assertThrows(AuthenticationException.class, () -> authService.login(loginDto2));
        LoginDto loginDto3 = LoginDto
                .builder()
                .username("user")
                .passwordHash("incorrect_hash")
                .build();
        assertThrows(AuthenticationException.class, () -> authService.login(loginDto3));
    }

    @Test
    void createUser() {
        UserDto userDto = UserDto
                .builder()
                .username("newbie")
                .passwordHash("totally_secret_hash")
                .accessLevel(AccessLevel.USER.name())
                .build();
        authService.createUser(userDto);
        UserDto userDtoFromDB = authService.getUserByUsername("newbie");
        userDto.setId(null);
        userDtoFromDB.setId(null);
        assertEquals(userDto, userDtoFromDB);
    }

    @Test
    void getUser() {
        UserDto userDto = UserDto
                .builder()
                .username("user")
                .passwordHash("8AC76453D769D4FD14B3F41AD4933F9BD64321972CD002DE9B847E117435B08B")
                .accessLevel(AccessLevel.USER.name())
                .build();
        UserDto userDtoFromDB = authService.getUser(authService.getUserByUsername("user").getId());
        userDto.setId(null);
        userDtoFromDB.setId(null);
        assertEquals(userDto, userDtoFromDB);
    }

    @Test
    void updateMe() throws JsonProcessingException {
        UserDto userDto = UserDto
                .builder()
                .username("user")
                .passwordHash("totally_secret_hash")
                .accessLevel(AccessLevel.ADMIN.name())
                .build();
        LoginDto loginDto = LoginDto
                .builder()
                .username("user")
                .passwordHash("8AC76453D769D4FD14B3F41AD4933F9BD64321972CD002DE9B847E117435B08B")
                .build();
        String accessToken = authService.login(loginDto);
        authService.updateMe(userDto, accessToken);
        UserDto userDtoFromDB = authService.getUserByUsername("user");
        userDto.setId(null);
        userDtoFromDB.setId(null);
        assertEquals(userDto, userDtoFromDB);
    }

    @Test
    void deleteUser() {
        authService.deleteUser(authService.getUserByUsername("user").getId());
        assertThrows(DatabaseException.class, () -> authService.getUserByUsername("user"));
    }

    @Test
    void getAllUsers() {
        List<UserDto> userDtoList = authService.getAllUsers();
        for (UserDto userDto : userDtoList) {
            userDto.setId(null);
        }
        assertEquals(
                Set.of(
                        UserDto
                                .builder()
                                .id(null)
                                .username("admin")
                                .passwordHash("FB001DFCFFD1C899F3297871406242F097AECF1A5342CCF3EBCD116146188E4B")
                                .accessLevel(AccessLevel.ADMIN.name())
                                .build(),
                        UserDto
                                .builder()
                                .id(null)
                                .username("user")
                                .passwordHash("8AC76453D769D4FD14B3F41AD4933F9BD64321972CD002DE9B847E117435B08B")
                                .accessLevel(AccessLevel.USER.name())
                                .build()
                ),
                Set.copyOf(userDtoList)
        );
    }

    @AfterEach
    void tearDown() {
        authService = null;
        productsDB = null;
    }
}
