-- Tables:

CREATE TABLE baskets (
    id INTEGER NOT NULL AUTO_INCREMENT,
    uuid UUID NOT NULL,
    last_accessed TIMESTAMP NOT NULL,
	PRIMARY KEY (id),
	UNIQUE KEY (uuid)
);

CREATE TABLE products (
    id INTEGER NOT NULL AUTO_INCREMENT,
    code VARCHAR(16) NOT NULL,
    name VARCHAR(64) NOT NULL,
    price_in_cents INTEGER NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (code)
);

CREATE TABLE products_in_baskets (
	id INTEGER NOT NULL AUTO_INCREMENT,
    basket_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
	PRIMARY KEY (id),
    FOREIGN KEY (basket_id) REFERENCES baskets(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    UNIQUE KEY (basket_id, product_id)
);

CREATE TABLE pack_discounts (
    id INTEGER NOT NULL AUTO_INCREMENT,
    product_id INTEGER NOT NULL,
    items_bought INTEGER NOT NULL,
    free_items INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE bulk_discounts (
    id INTEGER NOT NULL AUTO_INCREMENT,
    product_id INTEGER NOT NULL,
    threshold INTEGER NOT NULL,
    new_price_in_cents INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Views:

CREATE VIEW view_baskets AS SELECT
    baskets.uuid,
    products.code,
    products.name,
    products.price_in_cents,
    products_in_baskets.quantity
FROM
    products_in_baskets
LEFT JOIN baskets
    ON baskets.id = products_in_baskets.basket_id 
LEFT JOIN products
    ON products.id = products_in_baskets.product_id;

-- Initial data:
    
INSERT INTO
	products(code, name, price_in_cents)
VALUES
	('VOUCHER', 'Cabify Voucher', 500),
	('TSHIRT', 'Cabify T-Shirt', 2000),
	('MUG', 'Cabify Coffee Mug', 750);
	
INSERT INTO
    pack_discounts(product_id, items_bought, free_items)
VALUES
    (1, 2, 1);

INSERT INTO
    bulk_discounts(product_id, threshold, new_price_in_cents)
VALUES
    (2, 3, 1900);
    