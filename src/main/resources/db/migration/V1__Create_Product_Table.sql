-- Step 1: Create the Product table
CREATE TABLE IF NOT EXISTS Product (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(100) NOT NULL,
    price DOUBLE,
    tax VARCHAR(100),
    type VARCHAR(100) NOT NULL,
    sales_account INT,
    purchase_account INT,
    aggregateVersion INT,
    createdAt VARCHAR(255),
    createdBy VARCHAR(255),
    modifiedAt VARCHAR(255),
    modifiedBy VARCHAR(255)
);
