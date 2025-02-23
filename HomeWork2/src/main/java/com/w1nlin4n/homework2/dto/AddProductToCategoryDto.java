package com.w1nlin4n.homework2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AddProductToCategoryDto {
    private String productName;
    private String categoryName;
}
