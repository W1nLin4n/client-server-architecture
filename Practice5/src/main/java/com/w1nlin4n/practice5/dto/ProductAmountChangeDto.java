package com.w1nlin4n.practice5.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAmountChangeDto {
    private String productName;
    private Integer productAmount;
}
