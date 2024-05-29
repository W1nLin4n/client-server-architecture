package com.w1nlin4n.homework2.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Product {
    private String name;
    private String description;
    private String manufacturer;
    private Integer amount;
    private Double price;
}
