package com.w1nlin4n.practice3.dto;

import com.w1nlin4n.practice3.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CategoryDto {
    private String name;
    private String description;

    public Category toCategory() {
        return Category
                .builder()
                .name(name)
                .description(description)
                .build();
    }

    public static CategoryDto fromCategory(Category category) {
        return CategoryDto
                .builder()
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
