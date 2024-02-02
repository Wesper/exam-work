CREATE SCHEMA products;
CREATE TABLE products.products
(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    image_id VARCHAR(64) NOT NULL,
    title VARCHAR(128) NOT NULL,
    description VARCHAR(256) NOT NULL,
    subtitle TEXT NOT NULL,
    price NUMERIC NOT NULL,
    type VARCHAR(32) NOT NULL,
    measure VARCHAR(16) NOT NULL,
    unit_measure VARCHAR(16) NOT NULL,
    actual BOOLEAN NOT NULL
);
CREATE TABLE products.ratings
(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    rating FLOAT NOT NULL,
    user_id NUMERIC NOT NULL,
    product_id NUMERIC NOT NULL
);
CREATE TABLE products.reviews
(
    id LONG AUTO_INCREMENT PRIMARY KEY,
    user_id NUMERIC NOT NULL,
    product_id NUMERIC NOT NULL,
    review TEXT NOT NULL
);