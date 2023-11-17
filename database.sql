CREATE TABLE users(
    id BIGSERIAL PRIMARY KEY ,
    username VARCHAR(100) UNIQUE NOT NULL ,
    email VARCHAR(100) UNIQUE NOT NULL ,
    password VARCHAR NOT NULL ,
    address VARCHAR NOT NULL
);

SELECT * FROM users;

CREATE TABLE roles(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(100) UNIQUE NOT NULL
);

SELECT * FROM roles;

INSERT INTO roles(id, name) VALUES (1, 'CUSTOMER');
INSERT INTO roles(id, name) VALUES (2, 'MERCHANT');

SELECT * FROM roles;

CREATE TABLE user_roles(
    user_id BIGINT NOT NULL ,
    role_id INT NOT NULL ,
    CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_roles FOREIGN KEY (role_id) REFERENCES roles(id)
);

SELECT * FROM user_roles;

CREATE TABLE merchants(
      code BIGSERIAL PRIMARY KEY ,
      user_id BIGINT NOT NULL ,
      name VARCHAR(100) NOT NULL UNIQUE ,
      location VARCHAR(100) NOT NULL ,
      open BOOLEAN NOT NULL DEFAULT FALSE,
      CONSTRAINT fk_merchants_users
          FOREIGN KEY (user_id) REFERENCES users(id)
);


SELECT * FROM merchants;

CREATE TABLE products(
     code BIGSERIAL PRIMARY KEY ,
     merchant_code BIGINT NOT NULL ,
     name VARCHAR(100) NOT NULL ,
     price BIGINT NOT NULL ,
     CONSTRAINT fk_products_merchants
         FOREIGN KEY (merchant_code) REFERENCES merchants(code)
);


SELECT * FROM products;

CREATE TABLE orders(
       id BIGSERIAL PRIMARY KEY ,
       user_id BIGINT NOT NULL ,
       destination VARCHAR(100) NOT NULL ,
       time DATE NOT NULL ,
       completed BOOLEAN DEFAULT FALSE,
       CONSTRAINT fk_orders_users
           FOREIGN KEY (user_id) REFERENCES users(id)
);

SELECT * FROM orders;

CREATE TABLE order_details(
      id BIGSERIAL PRIMARY KEY ,
      order_id BIGINT NOT NULL ,
      product_code BIGINT NOT NULL ,
      quantity INT NOT NULL ,
      total_price BIGINT NOT NULL ,
      CONSTRAINT fk_order_details_orders FOREIGN KEY (order_id) REFERENCES orders(id),
      CONSTRAINT fk_order_details_products FOREIGN KEY (product_code) REFERENCES products(code)
);

SELECT * FROM order_details;