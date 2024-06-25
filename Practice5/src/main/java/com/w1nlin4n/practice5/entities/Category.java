package com.w1nlin4n.practice5.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Category {
    private String name;
    private String description;
}
