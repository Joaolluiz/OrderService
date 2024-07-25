CREATE TABLE orders (
    id BIGINT NOT NULL AUTO_INCREMENT,
    client_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(13) NOT NULL,
    address VARCHAR(100) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    total_value DECIMAL(19, 2) NOT NULL CHECK (total_value > 0),
    status VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);