package com.w1nlin4n.practice5.dto;

import com.w1nlin4n.practice5.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Integer id;
    private String username;
    private String passwordHash;
    private String accessLevel;

    public User toUser() {
        return User
                .builder()
                .id(id)
                .username(username)
                .passwordHash(passwordHash)
                .accessLevel(accessLevel)
                .build();
    }

    public static UserDto fromUser(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .username(user.getUsername())
                .passwordHash(user.getPasswordHash())
                .accessLevel(user.getAccessLevel())
                .build();
    }
}
