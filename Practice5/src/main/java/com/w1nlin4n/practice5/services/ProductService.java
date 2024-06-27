package com.w1nlin4n.practice5.services;

import com.w1nlin4n.practice5.database.ProductsDB;
import com.w1nlin4n.practice5.dto.ProductDto;
import com.w1nlin4n.practice5.entities.Product;
import com.w1nlin4n.practice5.exceptions.LogicException;
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

    public ProductDto getProduct(Integer id) {
        return ProductDto.fromProduct(productsDB.getProduct(id));
    }

    public ProductDto getProductByName(String name) {
        return ProductDto.fromProduct(productsDB.getProductByName(name));
    }

    public void updateProduct(Integer id, ProductDto product) {
        if (product.getAmount() < 0)
            throw new LogicException("Product amount cannot be negative", null);
        if (product.getPrice() < 0)
            throw new LogicException("Product price cannot be negative", null);
        productsDB.updateProduct(id, product.toProduct());
    }

    public void deleteProduct(Integer id) {
        productsDB.deleteProduct(id);
    }

    public void addAmountToProduct(Integer id, Integer amount) {
        synchronized (productsDB) {
            Product product = productsDB.getProduct(id);
            product.setAmount(product.getAmount() + amount);
            productsDB.updateProduct(id, product);
        }
    }

    public void removeAmountFromProduct(Integer id, Integer amount) {
        synchronized (productsDB) {
            Product product = productsDB.getProduct(id);
            product.setAmount(product.getAmount() - amount);
            if (product.getAmount() < 0)
                throw new LogicException("Product amount cannot be negative", null);
            productsDB.updateProduct(id, product);
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
