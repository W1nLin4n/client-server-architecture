package com.w1nlin4n.practice5.controllers;

import com.w1nlin4n.practice5.dto.ProductAmountChangeDto;
import com.w1nlin4n.practice5.controllers.security.Security;
import com.w1nlin4n.practice5.dto.ProductDto;
import com.w1nlin4n.practice5.networking.message.MessageCommand;
import com.w1nlin4n.practice5.services.ProductService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Controller(service = ProductService.class)
public class ProductController {
    private final ProductService productService;

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product", method = HttpMethod.POST, onSuccess = HttpCode.CREATED)
        productService.createProduct(product);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}", method = HttpMethod.GET, onFailure = HttpCode.NOT_FOUND)
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}", method = HttpMethod.PUT)
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}", method = HttpMethod.DELETE)
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}/add/{amount}", method = HttpMethod.PUT)
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}/remove/{amount}", method = HttpMethod.PUT)
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/all", method = HttpMethod.GET)
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
