package com.w1nlin4n.project.controllers;

import com.w1nlin4n.project.controllers.endpoint.Endpoint;
import com.w1nlin4n.project.networking.HttpCode;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.controllers.endpoint.params.Body;
import com.w1nlin4n.project.controllers.endpoint.params.Path;
import com.w1nlin4n.project.controllers.security.AccessLevel;
import com.w1nlin4n.project.controllers.security.Security;
import com.w1nlin4n.project.controllers.security.params.Token;
import com.w1nlin4n.project.dto.LoginDto;
import com.w1nlin4n.project.dto.UserDto;
import com.w1nlin4n.project.services.AuthService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Controller(service = AuthService.class)
public class AuthController {
    private final AuthService authService;

    @Security(level = {AccessLevel.USER, AccessLevel.ADMIN})
    @Endpoint(path = "/auth/me", method = HttpMethod.GET)
    public UserDto me(@Token String accessToken) { return authService.me(accessToken); }

    @Security(level = {AccessLevel.NON_USER})
    @Endpoint(path = "/auth/login", method = HttpMethod.POST)
    public String login(@Body LoginDto loginDto) { return authService.login(loginDto); }

    @Security(level = {AccessLevel.ADMIN})
    @Endpoint(path = "/auth", method = HttpMethod.POST, onSuccess = HttpCode.CREATED)
    public void createUser(@Body UserDto userDto) { authService.createUser(userDto); }

    @Security(level = {AccessLevel.ADMIN})
    @Endpoint(path = "/auth/{id}", method = HttpMethod.GET, onFailure = HttpCode.NOT_FOUND)
    public UserDto getUser(@Path(name = "id") Integer id) { return authService.getUser(id); }

    @Security(level = {AccessLevel.USER, AccessLevel.ADMIN})
    @Endpoint(path = "/auth/me", method = HttpMethod.PUT)
    public void updateMe(@Body UserDto userDto, @Token String accessToken) { authService.updateMe(userDto, accessToken); }

    @Security(level = {AccessLevel.ADMIN})
    @Endpoint(path = "/auth/{id}", method = HttpMethod.DELETE)
    public void deleteUser(@Path(name = "id") Integer id) { authService.deleteUser(id); }

    @Security(level = {AccessLevel.ADMIN})
    @Endpoint(path = "/auth/all", method = HttpMethod.GET)
    public List<UserDto> getAllUsers() { return authService.getAllUsers(); }
}
