CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    billing_address VARCHAR(255) NOT NULL,
    customer_id BIGINT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    order_status VARCHAR(255) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    shipping_address VARCHAR(255) NOT NULL,
    total_amount FLOAT(8) NOT NULL
);

CREATE TABLE IF NOT EXISTS order_item (
    id SERIAL PRIMARY KEY,
    price FLOAT(8) NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    order_id BIGINT NOT NULL
);