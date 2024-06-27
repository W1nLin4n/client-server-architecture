package com.w1nlin4n.practice5.controllers;

import com.w1nlin4n.practice5.dto.ProductAmountChangeDto;
import com.w1nlin4n.practice5.dto.ProductDto;
import com.w1nlin4n.practice5.networking.message.MessageCommand;
import com.w1nlin4n.practice5.services.ProductService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Controller(service = ProductService.class)
public class ProductController {
    private final ProductService productService;

    @Endpoint(command = MessageCommand.CREATE_PRODUCT, dto = ProductDto.class)
    public void createProduct(ProductDto product) {
    @Endpoint(path = "/product", method = HttpMethod.POST, onSuccess = HttpCode.CREATED)
        productService.createProduct(product);
    }

    @Endpoint(command = MessageCommand.GET_PRODUCT, dto = String.class)
    public ProductDto getProduct(String productName) {
        return productService.getProduct(productName);
    @Endpoint(path = "/product/{id}", method = HttpMethod.GET, onFailure = HttpCode.NOT_FOUND)
    }

    @Endpoint(command = MessageCommand.UPDATE_PRODUCT, dto = ProductDto.class)
    public void updateProduct(ProductDto product) {
        productService.updateProduct(product);
    @Endpoint(path = "/product/{id}", method = HttpMethod.PUT)
    }

    @Endpoint(command = MessageCommand.DELETE_PRODUCT, dto = String.class)
    public void deleteProduct(String productName) {
        productService.deleteProduct(productName);
    @Endpoint(path = "/product/{id}", method = HttpMethod.DELETE)
    }

    @Endpoint(command = MessageCommand.ADD_PRODUCT_AMOUNT, dto = ProductAmountChangeDto.class)
    public void addAmountToProduct(ProductAmountChangeDto productAmountChangeDto) {
        productService.addAmountToProduct(productAmountChangeDto);
    @Endpoint(path = "/product/{id}/add/{amount}", method = HttpMethod.PUT)
    }

    @Endpoint(command = MessageCommand.REMOVE_PRODUCT_AMOUNT, dto = ProductAmountChangeDto.class)
    public void removeAmountFromProduct(ProductAmountChangeDto productAmountChangeDto) {
        productService.removeAmountFromProduct(productAmountChangeDto);
    @Endpoint(path = "/product/{id}/remove/{amount}", method = HttpMethod.PUT)
    }

    @Endpoint(command = MessageCommand.GET_ALL_PRODUCTS, dto = ProductDto.class)
    @Endpoint(path = "/product/all", method = HttpMethod.GET)
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
