package com.w1nlin4n.practice3.controllers;

import com.w1nlin4n.practice3.dto.ProductAmountChangeDto;
import com.w1nlin4n.practice3.dto.ProductDto;
import com.w1nlin4n.practice3.networking.message.MessageCommand;
import com.w1nlin4n.practice3.services.ProductService;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Controller(service = ProductService.class)
public class ProductController {
    private final ProductService productService;

    @Endpoint(command = MessageCommand.CREATE_PRODUCT, dto = ProductDto.class)
    public void createProduct(ProductDto product) {
        productService.createProduct(product);
    }

    @Endpoint(command = MessageCommand.GET_PRODUCT, dto = String.class)
    public ProductDto getProduct(String productName) {
        return productService.getProduct(productName);
    }

    @Endpoint(command = MessageCommand.UPDATE_PRODUCT, dto = ProductDto.class)
    public void updateProduct(ProductDto product) {
        productService.updateProduct(product);
    }

    @Endpoint(command = MessageCommand.DELETE_PRODUCT, dto = String.class)
    public void deleteProduct(String productName) {
        productService.deleteProduct(productName);
    }

    @Endpoint(command = MessageCommand.ADD_PRODUCT_AMOUNT, dto = ProductAmountChangeDto.class)
    public void addAmountToProduct(ProductAmountChangeDto productAmountChangeDto) {
        productService.addAmountToProduct(productAmountChangeDto);
    }

    @Endpoint(command = MessageCommand.REMOVE_PRODUCT_AMOUNT, dto = ProductAmountChangeDto.class)
    public void removeAmountFromProduct(ProductAmountChangeDto productAmountChangeDto) {
        productService.removeAmountFromProduct(productAmountChangeDto);
    }

    @Endpoint(command = MessageCommand.GET_ALL_PRODUCTS, dto = ProductDto.class)
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }
}
