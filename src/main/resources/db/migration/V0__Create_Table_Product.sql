CREATE TABLE product (
    id BIGINT NOT NULL AUTO_INCREMENT,
    sku VARCHAR(10) NOT NULL,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(300),
    value DECIMAL(19, 2) NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT sku_unique UNIQUE (sku),
    PRIMARY KEY (id)
);