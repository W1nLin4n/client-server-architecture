package com.w1nlin4n.practice5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddProductToCategoryDto {
    private String productName;
    private String categoryName;
}
