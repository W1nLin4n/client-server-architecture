package com.w1nlin4n.project.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.w1nlin4n.project.database.ProductsDB;
import com.w1nlin4n.project.dto.LoginDto;
import com.w1nlin4n.project.dto.UserDto;
import com.w1nlin4n.project.entities.User;
import com.w1nlin4n.project.exceptions.AuthenticationException;
import com.w1nlin4n.project.exceptions.DatabaseException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuthService {
    private final ProductsDB productsDB;
    private final JWTVerifier jwtVerifier;
    private final Algorithm algorithm;

    public AuthService(ProductsDB productsDB) {
        this.productsDB = productsDB;
        algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET"));
        jwtVerifier = JWT
                .require(algorithm)
                .withIssuer("W1nLin4n")
                .build();
    }

    public UserDto me(String accessToken) {
        DecodedJWT jwt = jwtVerifier.verify(accessToken);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jwt.getClaim("user").asString(), UserDto.class);
        } catch (JsonProcessingException e) {
            throw new AuthenticationException("Can't process JWT payload", e);
        }

    }

    public String login(LoginDto loginDto) {
        User user;
        try {
            user = productsDB.getUserByUsername(loginDto.getUsername());
        } catch (DatabaseException e) {
            throw new AuthenticationException("Invalid username or password", e);
        }
        if (!user.getPasswordHash().equals(loginDto.getPasswordHash()))
            throw new AuthenticationException("Invalid username or password", null);

        UserDto userDto = UserDto.fromUser(user);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return JWT
                    .create()
                    .withIssuer("W1nLin4n")
                    .withClaim("user", objectMapper.writeValueAsString(userDto))
                    .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                    .sign(algorithm);
        } catch (JsonProcessingException e) {
            throw new AuthenticationException("Can't process user", e);
        }
    }

    public void createUser(UserDto userDto) {
        productsDB.createUser(userDto.toUser());
    }

    public UserDto getUser(Integer id) {
        return UserDto.fromUser(productsDB.getUser(id));
    }

    public UserDto getUserByUsername(String username) {
        return UserDto.fromUser(productsDB.getUserByUsername(username));
    }

    public void updateMe(UserDto userDto, String accessToken) {
        UserDto tokenUser = me(accessToken);
        productsDB.updateUser(tokenUser.getId(), userDto.toUser());
    }

    public void deleteUser(Integer id) {
        productsDB.deleteUser(id);
    }

    public List<UserDto> getAllUsers() {
        List<UserDto> result = new ArrayList<>();
        for (User user : productsDB.getAllUsers()) {
            result.add(UserDto.fromUser(user));
        }
        return result;
    }
}
