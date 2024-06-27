package com.w1nlin4n.practice5.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String description;
    private String manufacturer;
    private Integer amount;
    private Double price;
}
