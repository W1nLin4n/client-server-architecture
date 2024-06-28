package com.w1nlin4n.practice5.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Integer id;
    private String username;
    private String passwordHash;
    private String accessLevel;
}
