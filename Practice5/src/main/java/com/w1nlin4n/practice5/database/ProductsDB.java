package com.w1nlin4n.practice5.database;


import com.w1nlin4n.practice5.entities.Category;
import com.w1nlin4n.practice5.entities.Product;
import com.w1nlin4n.practice5.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDB {
    private final Connection connection;

    public ProductsDB(String dbUrl) {
        try {
            connection = DriverManager.getConnection(dbUrl);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseException("Could not connect to the database", e);
        }
        MigrationsManager migrationsManager = new MigrationsManager(connection);
        migrationsManager.migrate();
    }

    public void createCategory(Category category) {
        synchronized (connection) {
            try (Statement statement = connection.createStatement()) {
                String sql =
                        "INSERT INTO category (" +
                            "name, " +
                            "description" +
                        ") " +
                        "VALUES ('" +
                            category.getName() + "', '" +
                            category.getDescription() +
                        "');";
                statement.executeUpdate(sql);
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not create category", e);
            }
        }
    }

    public Category getCategory(Integer id) {
        Category category;
        synchronized (connection) {
            try {
                Statement statement = connection.createStatement();
                String sql =
                        "SELECT * " +
                        "FROM category " +
                        "WHERE id = " + id + ";";
                ResultSet result = statement.executeQuery(sql);
                if(!result.next())
                    throw new DatabaseException("Could not find a category with such id", null);
                category = Category
                        .builder()
                        .id(result.getInt("id"))
                        .name(result.getString("name"))
                        .description(result.getString("description"))
                        .build();
                statement.close();
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not get category", e);
            }
        }
        return category;
    }

    public Category getCategoryByName(String name) {
        Category category;
        synchronized (connection) {
            try {
                Statement statement = connection.createStatement();
                String sql =
                        "SELECT * " +
                        "FROM category " +
                        "WHERE name = '" + name + "';";
                ResultSet result = statement.executeQuery(sql);
                if(!result.next())
                    throw new DatabaseException("Could not find a category with such name", null);
                category = Category
                        .builder()
                        .id(result.getInt("id"))
                        .name(result.getString("name"))
                        .description(result.getString("description"))
                        .build();
                statement.close();
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not get category", e);
            }
        }
        return category;
    }

    public void updateCategory(Integer id, Category category) {
        synchronized (connection) {
            try {
                Statement statement = connection.createStatement();
                String sql =
                        "UPDATE category " +
                        "SET " +
                            "name = '" + category.getName() + "', " +
                            "description = '" + category.getDescription() + "' " +
                        "WHERE id = " + id + ";";
                statement.executeUpdate(sql);
                statement.close();
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not update category", e);
            }
        }
    }

    public void deleteCategory(Integer id) {
        synchronized (connection) {
            try {
                Statement statement = connection.createStatement();
                String sql =
                        "DELETE FROM category " +
                        "WHERE id = " + id + ";";
                statement.executeUpdate(sql);
                statement.close();
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not delete category", e);
            }
        }
    }

    public List<Product> getAllProductsFromCategory(Integer id) {
        List<Product> products = new ArrayList<>();
        synchronized (connection) {
            try {
                Statement statement = connection.createStatement();
                String sql =
                        "SELECT * " +
                        "FROM product " +
                        "WHERE category_id = " + id + ";";
                ResultSet result = statement.executeQuery(sql);
                while (result.next()) {
                    Product product = Product
                            .builder()
                            .id(result.getInt("id"))
                            .categoryId(result.getInt("category_id"))
                            .name(result.getString("name"))
                            .description(result.getString("description"))
                            .manufacturer(result.getString("manufacturer"))
                            .amount(result.getInt("amount"))
                            .price(result.getDouble("price"))
                            .build();
                    products.add(product);
                }
                statement.close();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not get all products", e);
            }
        }
        return products;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        synchronized (connection) {
            try {
                Statement statement = connection.createStatement();
                String sql =
                        "SELECT * " +
                        "FROM category;";
                ResultSet result = statement.executeQuery(sql);
                while (result.next()) {
                    Category category = Category
                            .builder()
                            .id(result.getInt("id"))
                            .name(result.getString("name"))
                            .description(result.getString("description"))
                            .build();
                    categories.add(category);
                }
                statement.close();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not get all categories", e);
            }
        }
        return categories;
    }

    public void createProduct(Product product) {
        synchronized (connection) {
            try (Statement statement = connection.createStatement()) {
                String sql =
                        "INSERT INTO product (" +
                            "category_id, " +
                            "name, " +
                            "description, " +
                            "manufacturer, " +
                            "amount, " +
                            "price" +
                        ") " +
                        "VALUES (" +
                            product.getCategoryId() + ", '" +
                            product.getName() + "', '" +
                            product.getDescription() + "', '" +
                            product.getManufacturer() + "', '" +
                            product.getAmount() + "', '" +
                            product.getManufacturer() + "', " +
                            product.getAmount() + ", " +
                            product.getPrice() +
                        ");";
                statement.executeUpdate(sql);
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not create product", e);
            }
        }
    }

    public Product getProduct(Integer id) {
        Product product;
        synchronized (connection) {
            try {
                Statement statement = connection.createStatement();
                String sql =
                        "SELECT * " +
                        "FROM product " +
                        "WHERE id = " + id + ";";
                ResultSet result = statement.executeQuery(sql);
                if(!result.next())
                    throw new DatabaseException("Could not find a product with such id", null);
                product = Product
                        .builder()
                        .id(result.getInt("id"))
                        .categoryId(result.getInt("category_id"))
                        .name(result.getString("name"))
                        .description(result.getString("description"))
                        .manufacturer(result.getString("manufacturer"))
                        .amount(result.getInt("amount"))
                        .price(result.getDouble("price"))
                        .build();
                statement.close();
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not get product", e);
            }
        }
        return product;
    }

    public Product getProductByName(String name) {
        Product product;
        synchronized (connection) {
            try {
                Statement statement = connection.createStatement();
                String sql =
                        "SELECT * " +
                        "FROM product " +
                        "WHERE name = '" + name + "';";
                ResultSet result = statement.executeQuery(sql);
                if(!result.next())
                    throw new DatabaseException("Could not find a product with such name", null);
                product = Product
                        .builder()
                        .id(result.getInt("id"))
                        .categoryId(result.getInt("category_id"))
                        .name(result.getString("name"))
                        .description(result.getString("description"))
                        .manufacturer(result.getString("manufacturer"))
                        .amount(result.getInt("amount"))
                        .price(result.getDouble("price"))
                        .build();
                statement.close();
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not get product", e);
            }
        }
        return product;
    }

    public void updateProduct(Integer id, Product product) {
        synchronized (connection) {
            try {
                Statement statement = connection.createStatement();
                String sql =
                        "UPDATE product " +
                        "SET " +
                            "name = '" + product.getName() + "', " +
                            "description = '" + product.getDescription() + "', " +
                            "manufacturer = '" + product.getManufacturer() + "', " +
                            "amount = " + product.getAmount() + ", " +
                            "price = " + product.getPrice() + " " +
                        "WHERE id = " + id + ";";
                statement.executeUpdate(sql);
                statement.close();
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not update product", e);
            }
        }
    }

    public void deleteProduct(Integer id) {
        synchronized (connection) {
            try {
                Statement statement = connection.createStatement();
                String sql =
                        "DELETE FROM product " +
                        "WHERE id = " + id + ";";
                statement.executeUpdate(sql);
                statement.close();
                connection.commit();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not delete product", e);
            }
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        synchronized (connection) {
            try {
                Statement statement = connection.createStatement();
                String sql =
                        "SELECT * " +
                        "FROM product;";
                ResultSet result = statement.executeQuery(sql);
                while (result.next()) {
                    Product product = Product
                            .builder()
                            .id(result.getInt("id"))
                            .categoryId(result.getInt("category_id"))
                            .name(result.getString("name"))
                            .description(result.getString("description"))
                            .manufacturer(result.getString("manufacturer"))
                            .amount(result.getInt("amount"))
                            .price(result.getDouble("price"))
                            .build();
                    products.add(product);
                }
                statement.close();
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    throw new DatabaseException("Could not rollback transaction", e1);
                }
                throw new DatabaseException("Could not get all products", e);
            }
        }
        return products;
    }
}
