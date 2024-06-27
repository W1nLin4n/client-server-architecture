package com.w1nlin4n.practice5.dto;

import com.w1nlin4n.practice5.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private Integer id;
    private String name;
    private String description;

    public Category toCategory() {
        return Category
                .builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    public static CategoryDto fromCategory(Category category) {
        return CategoryDto
                .builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }
}
