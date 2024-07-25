CREATE TABLE `order_product` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    quantity INT NOT NULL CHECK (quantity > 0),
    product_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES product(id),
    FOREIGN KEY (order_id) REFERENCES orders(id)
);