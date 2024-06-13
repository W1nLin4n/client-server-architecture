package com.w1nlin4n.practice3.database;


import com.w1nlin4n.practice3.entities.Category;
import com.w1nlin4n.practice3.entities.Product;
import lombok.Synchronized;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductsDB extends Database {

    public ProductsDB() {
        super("store", new HashMap<>());
        createTable("product");
        createTable("category");
        createTable("product_category");
    }

    @Synchronized
    public void createCategory(Category category) {
        Table table = getTable("category");
        HashMap<String, Object> values = new HashMap<>();
        values.put("name", category.getName());
        values.put("description", category.getDescription());
        Row row = new Row(category.getName(), values);
        table.insertRow(row);
    }

    @Synchronized
    public Category getCategory(String categoryName) {
        Table table = getTable("category");
        Row row = table.getRow(categoryName);
        HashMap<String, Object> values = row.getValues();
        return Category
                .builder()
                .name((String) values.get("name"))
                .description((String) values.get("description"))
                .build();
    }

    @Synchronized
    public void updateCategory(Category category) {
        Table table = getTable("category");
        HashMap<String, Object> values = new HashMap<>();
        values.put("name", category.getName());
        values.put("description", category.getDescription());
        Row row = new Row(category.getName(), values);
        table.updateRow(row);
    }

    @Synchronized
    public void deleteCategory(String categoryName) {
        Table table = getTable("category");
        table.deleteRow(categoryName);
        List<Product> products = getAllProductsFromCategory(categoryName);
        for (Product product : products) {
            deleteProduct(product.getName());
        }
    }

    @Synchronized
    public List<Category> getAllCategories() {
        Table table = getTable("category");
        List<Category> categories = new ArrayList<>();
        for (Row row : table.getRows().values()) {
            Category category = Category
                    .builder()
                    .name((String) row.getValues().get("name"))
                    .description((String) row.getValues().get("description"))
                    .build();
            categories.add(category);
        }
        return categories;
    }

    @Synchronized
    public void createProduct(Product product) {
        Table table = getTable("product");
        HashMap<String, Object> values = new HashMap<>();
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("manufacturer", product.getManufacturer());
        values.put("amount", product.getAmount());
        values.put("price", product.getPrice());
        Row row = new Row(product.getName(), values);
        table.insertRow(row);
    }

    @Synchronized
    public Product getProduct(String productName) {
        Table table = getTable("product");
        Row row = table.getRow(productName);
        HashMap<String, Object> values = row.getValues();
        return Product
                .builder()
                .name((String) values.get("name"))
                .description((String) values.get("description"))
                .manufacturer((String) values.get("manufacturer"))
                .amount((Integer) values.get("amount"))
                .price((Double) values.get("price"))
                .build();
    }

    @Synchronized
    public void updateProduct(Product product) {
        Table table = getTable("product");
        HashMap<String, Object> values = new HashMap<>();
        values.put("name", product.getName());
        values.put("description", product.getDescription());
        values.put("manufacturer", product.getManufacturer());
        values.put("amount", product.getAmount());
        values.put("price", product.getPrice());
        Row row = new Row(product.getName(), values);
        table.updateRow(row);
    }

    @Synchronized
    public void deleteProduct(String productName) {
        Table table = getTable("product");
        table.deleteRow(productName);
        Table product_category = getTable("product_category");
        product_category.deleteRow(productName);
    }

    @Synchronized
    public List<Product> getAllProducts() {
        Table table = getTable("product");
        List<Product> products = new ArrayList<>();
        for (Row row : table.getRows().values()) {
            Product product = Product
                    .builder()
                    .name((String) row.getValues().get("name"))
                    .description((String) row.getValues().get("description"))
                    .manufacturer((String) row.getValues().get("manufacturer"))
                    .amount((Integer) row.getValues().get("amount"))
                    .price((Double) row.getValues().get("price"))
                    .build();
            products.add(product);
        }
        return products;
    }

    @Synchronized
    public void addProductToCategory(String productName, String categoryName) {
        Table table = getTable("product_category");
        HashMap<String, Object> values = new HashMap<>();
        values.put("product", productName);
        values.put("category", categoryName);
        Row row = new Row(productName, values);
        table.insertRow(row);
    }

    @Synchronized
    public List<Product> getAllProductsFromCategory(String categoryName) {
        Table table = getTable("product_category");
        List<Product> products = new ArrayList<>();
        for(Row row : table.getRows().values()) {
            if(row.getValues().get("category").equals(categoryName)) {
                products.add(getProduct((String) row.getValues().get("product")));
            }
        }
        return products;
    }
}
