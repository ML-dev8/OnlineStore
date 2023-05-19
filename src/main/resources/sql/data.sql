-- users table
INSERT INTO users (name, password, enabled)
VALUES ('admin', '$2a$10$ZuShQK.Gir5AV1lkPx/bCO0bnUOYLVKEXvc.FG48PGf0R0Ecxot7y', 1),
       ('user', '$2a$10$Yu8F6h01tPTNf6FyXe00W.3TJgVESDtFkI26xZsGmpd026jH.uOAK', 1);

-- roles table
INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER');

-- users_roles table
INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (2, 2);

-- products table
INSERT INTO products (name, price, amount)
VALUES ('products_1', 14, 0),
       ('products_2', 17, 2),
       ('products_3', 15, 3),
       ('products_4', 20, 1),
       ('products_5', 17, 0),
       ('products_6', 15, 3);

-- orders table
INSERT INTO orders (name, user_id, status)
VALUES ('orders_1', 1, 'N'),
       ('orders_2', 2, 'P');

-- orders_products table
INSERT INTO orders_products (order_id, product_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 3);
