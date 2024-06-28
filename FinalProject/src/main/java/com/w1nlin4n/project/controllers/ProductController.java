package com.w1nlin4n.project.controllers;

import com.w1nlin4n.project.controllers.endpoint.Endpoint;
import com.w1nlin4n.project.networking.HttpCode;
import com.w1nlin4n.project.networking.HttpMethod;
import com.w1nlin4n.project.controllers.endpoint.params.Body;
import com.w1nlin4n.project.controllers.endpoint.params.Path;
import com.w1nlin4n.project.controllers.security.AccessLevel;
import com.w1nlin4n.project.controllers.security.Security;
import com.w1nlin4n.project.dto.ProductDto;
import com.w1nlin4n.project.services.ProductService;
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
        return productService.getProduct(id);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}", method = HttpMethod.PUT)
    public void updateProduct(@Path(name = "id") Integer id, @Body ProductDto product) {
        productService.updateProduct(id, product);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}", method = HttpMethod.DELETE)
    public void deleteProduct(@Path(name = "id") Integer id) {
        productService.deleteProduct(id);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}/add/{amount}", method = HttpMethod.PUT)
    public void addAmountToProduct(@Path(name = "id") Integer id, @Path(name = "amount") Integer amount) {
        productService.addAmountToProduct(id, amount);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/{id}/remove/{amount}", method = HttpMethod.PUT)
    public void removeAmountFromProduct(@Path(name = "id") Integer id, @Path(name = "amount") Integer amount) {
        productService.removeAmountFromProduct(id, amount);
    }

    @Security(level = {AccessLevel.USER})
    @Endpoint(path = "/product/all", method = HttpMethod.GET)
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
