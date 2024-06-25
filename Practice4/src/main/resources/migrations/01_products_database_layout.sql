CREATE TABLE product(
    name VARCHAR(100) PRIMARY KEY,
    description VARCHAR(255),
    manufacturer VARCHAR(100),
    amount INTEGER,
    price DECIMAL(10, 2)
);

CREATE TABLE category(
    name VARCHAR(100) PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE product_category(
    product VARCHAR(100) PRIMARY KEY,
    category VARCHAR(100),
    FOREIGN KEY (product)
        REFERENCES product(name)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    FOREIGN KEY (category)
        REFERENCES category(name)
            ON DELETE CASCADE
            ON UPDATE CASCADE
);