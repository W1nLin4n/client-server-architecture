package com.w1nlin4n.practice5.controllers;

import com.w1nlin4n.practice5.dto.AddProductToCategoryDto;
import com.w1nlin4n.practice5.controllers.security.Security;
import com.w1nlin4n.practice5.dto.CategoryDto;
import com.w1nlin4n.practice5.dto.ProductDto;
import com.w1nlin4n.practice5.networking.message.MessageCommand;
import com.w1nlin4n.practice5.services.CategoryService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Controller(service = CategoryService.class)
public class CategoryController {
    private final CategoryService categoryService;

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category", method = HttpMethod.POST, onSuccess = HttpCode.CREATED)
        categoryService.createCategory(category);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category/{id}", method = HttpMethod.GET, onFailure = HttpCode.NOT_FOUND)
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category/{id}", method = HttpMethod.PUT)
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category/{id}", method = HttpMethod.DELETE)
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category/{id}/products", method = HttpMethod.GET)
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/category/all", method = HttpMethod.GET)
    public List<CategoryDto> getAllCategories() {
        return categoryService.getAllCategories();
    }

}
