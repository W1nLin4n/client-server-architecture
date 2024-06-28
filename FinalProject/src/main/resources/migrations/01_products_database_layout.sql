CREATE TABLE product(
    id INTEGER PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(255),
    manufacturer VARCHAR(100),
    amount INTEGER,
    price DECIMAL(10, 2),
    category_id INTEGER,
    FOREIGN KEY (category_id)
        REFERENCES category(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE (name)
);

CREATE TABLE category(
    id INTEGER PRIMARY KEY,
    name VARCHAR(100),
    description VARCHAR(255),
    UNIQUE (name)
);