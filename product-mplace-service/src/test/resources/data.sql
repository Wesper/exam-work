INSERT INTO products.products (id, image_id, title, description, subtitle, price, type, measure, unit_measure, actual)
VALUES (1, 'a.jpeg', 'Product 1', 'If you buy the product you win', 'Best of product', 1, 'A', '1', 'kg', true),
       (2, 'b.jpeg', 'Product 2', 'If you buy the product you win', 'Best of product', 2, 'A', '2', 'kg', true),
       (3, 'c.jpeg', 'Product 3', 'If you buy the product you win', 'Best of product', 3, 'A', '3', 'kg', true),
       (4, 'd.jpeg', 'Product 4', 'If you buy the product you win', 'Best of product', 4, 'A', '4', 'kg', true),
       (5, 'e.jpeg', 'Product 5', 'If you buy the product you win', 'Best of product', 5, 'B', '5', 'ml', true),
       (6, 'f.jpeg', 'Product 6', 'If you buy the product you win', 'Best of product', 6, 'B', '6', 'ml', true);

INSERT INTO products.ratings (id, rating, user_id, product_id)
VALUES (1, 1, 1, 1),
       (2, 2, 1, 2),
       (3, 2, 2, 1),
       (4, 3, 3, 1),
       (5, 3, 4, 1),
       (6, 4, 4, 2),
       (7, 4, 4, 3),
       (8, 5, 5, 3),
       (9, 5, 5, 5),
       (10, 1, 5, 4);

INSERT INTO products.reviews (id, user_id, product_id, review)
VALUES (1, 1, 1, 'A'),
       (2, 2, 1, 'B'),
       (3, 2, 2, 'C'),
       (4, 3, 3, 'D'),
       (5, 3, 4, 'E'),
       (6, 4, 4, 'F'),
       (7, 4, 5, 'D'),
       (8, 5, 5, 'H'),
       (9, 5, 6, 'I'),
       (10, 1, 5, 'J');