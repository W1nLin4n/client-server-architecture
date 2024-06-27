package com.w1nlin4n.practice5.controllers;

import com.w1nlin4n.practice5.dto.ProductAmountChangeDto;
import com.w1nlin4n.practice5.controllers.endpoint.params.Body;
import com.w1nlin4n.practice5.controllers.endpoint.params.Path;
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
    public void createProduct(@Body ProductDto product) {
        productService.createProduct(product);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}", method = HttpMethod.GET, onFailure = HttpCode.NOT_FOUND)
    public ProductDto getProduct(@Path(name = "id") Integer id) {
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}", method = HttpMethod.PUT)
    public void updateProduct(@Path(name = "id") Integer id, @Body ProductDto product) {
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}", method = HttpMethod.DELETE)
    public void deleteProduct(@Path(name = "id") Integer id) {
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}/add/{amount}", method = HttpMethod.PUT)
    public void addAmountToProduct(@Path(name = "id") Integer id, @Path(name = "amount") Integer amount) {
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}/remove/{amount}", method = HttpMethod.PUT)
    public void removeAmountFromProduct(@Path(name = "id") Integer id, @Path(name = "amount") Integer amount) {
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/all", method = HttpMethod.GET)
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
