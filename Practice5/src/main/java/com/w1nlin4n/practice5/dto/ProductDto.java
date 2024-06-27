package com.w1nlin4n.practice5.dto;

import com.w1nlin4n.practice5.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String description;
    private String manufacturer;
    private Integer amount;
    private Double price;

    public Product toProduct() {
        return Product
                .builder()
                .id(id)
                .categoryId(categoryId)
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
                .id(product.getId())
                .categoryId(product.getCategoryId())
                .name(product.getName())
                .description(product.getDescription())
                .manufacturer(product.getManufacturer())
                .amount(product.getAmount())
                .price(product.getPrice())
                .build();
    }
}
