package com.w1nlin4n.homework2.dto;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
public class ProductAmountChangeDto {
    private String productName;
    private Integer productAmount;
}
