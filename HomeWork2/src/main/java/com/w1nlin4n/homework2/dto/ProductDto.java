package com.w1nlin4n.homework2.dto;

import com.w1nlin4n.homework2.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ProductDto {
    private String name;
    private String description;
    private String manufacturer;
    private Integer amount;
    private Double price;

    public Product toProduct() {
        return Product
                .builder()
                .name(name)
                .description(description)
                .manufacturer(manufacturer)
                .amount(amount)
                .price(price)
                .build();
    }

    public static ProductDto fromProduct(Product product) {
        return ProductDto
                .builder()
                .name(product.getName())
                .description(product.getDescription())
                .manufacturer(product.getManufacturer())
                .amount(product.getAmount())
                .price(product.getPrice())
                .build();
    }
}
