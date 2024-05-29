package com.w1nlin4n.homework2.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
public class ProductAmountChangeDto {
    private String productName;
    private Integer productAmount;
}
