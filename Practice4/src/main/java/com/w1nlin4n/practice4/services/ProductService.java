package com.w1nlin4n.practice4.services;

import com.w1nlin4n.practice4.database.ProductsDB;
import com.w1nlin4n.practice4.dto.ProductAmountChangeDto;
import com.w1nlin4n.practice4.dto.ProductDto;
import com.w1nlin4n.practice4.entities.Product;
import com.w1nlin4n.practice4.exceptions.LogicException;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ProductService {
    private final ProductsDB productsDB;

    public void createProduct(ProductDto product) {
        if (product.getAmount() < 0)
            throw new LogicException("Product amount cannot be negative", null);
        if (product.getPrice() < 0)
            throw new LogicException("Product price cannot be negative", null);
        productsDB.createProduct(product.toProduct());
    }

    public ProductDto getProduct(String productName) {
        return ProductDto.fromProduct(productsDB.getProduct(productName));
    }

    public void updateProduct(ProductDto product) {
        if (product.getAmount() < 0)
            throw new LogicException("Product amount cannot be negative", null);
        if (product.getPrice() < 0)
            throw new LogicException("Product price cannot be negative", null);
        productsDB.updateProduct(product.toProduct());
    }

    public void deleteProduct(String productName) {
        productsDB.deleteProduct(productName);
    }

    public void addAmountToProduct(ProductAmountChangeDto productAmountChangeDto) {
        synchronized (productsDB) {
            Product product = productsDB.getProduct(productAmountChangeDto.getProductName());
            product.setAmount(product.getAmount() + productAmountChangeDto.getProductAmount());
            productsDB.updateProduct(product);
        }
    }

    public void removeAmountFromProduct(ProductAmountChangeDto productAmountChangeDto) {
        synchronized (productsDB) {
            Product product = productsDB.getProduct(productAmountChangeDto.getProductName());
            product.setAmount(product.getAmount() - productAmountChangeDto.getProductAmount());
            if (product.getAmount() < 0)
                throw new LogicException("Product amount cannot be negative", null);
            productsDB.updateProduct(product);
        }
    }

    public List<ProductDto> getAllProducts() {
        List<ProductDto> result = new ArrayList<>();
        for (Product product : productsDB.getAllProducts()) {
            result.add(ProductDto.fromProduct(product));
        }
        return result;
    }
}
