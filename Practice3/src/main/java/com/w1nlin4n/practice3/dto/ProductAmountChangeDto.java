package com.w1nlin4n.practice3.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class ProductAmountChangeDto {
    private String productName;
    private Integer productAmount;
}
