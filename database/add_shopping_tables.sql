USE [BaiTapGiuaKy];
SET XACT_ABORT ON;
BEGIN TRANSACTION;
IF OBJECT_ID(N'dbo.customer_orders', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.customer_orders (
        id INT IDENTITY PRIMARY KEY,
        user_id INT NOT NULL REFERENCES dbo.users(id),
        recipient NVARCHAR(100) NOT NULL,
        phone VARCHAR(20) NOT NULL,
        address NVARCHAR(500) NOT NULL,
        note NVARCHAR(500) NULL,
        created_at DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
        status VARCHAR(20) NOT NULL DEFAULT 'NEW',
        payment_method VARCHAR(10) NOT NULL DEFAULT 'COD',
        total DECIMAL(18,2) NOT NULL CHECK (total >= 0),
        CONSTRAINT CK_order_status CHECK (status IN
            ('NEW','CONFIRMED','PREPARING','SHIPPING','DELIVERING','DELIVERED','CANCELLED','RETURNED')),
        CONSTRAINT CK_order_payment CHECK (payment_method = 'COD')
    );
    CREATE INDEX IX_orders_user_status ON dbo.customer_orders(user_id, status, created_at DESC);
END;
IF OBJECT_ID(N'dbo.order_items', N'U') IS NULL
BEGIN
    CREATE TABLE dbo.order_items (
        id INT IDENTITY PRIMARY KEY,
        order_id INT NOT NULL REFERENCES dbo.customer_orders(id),
        book_id INT NOT NULL,
        title NVARCHAR(200) NOT NULL,
        unit_price DECIMAL(18,2) NOT NULL CHECK (unit_price >= 0),
        quantity INT NOT NULL CHECK (quantity BETWEEN 1 AND 99)
    );
    CREATE INDEX IX_order_items_order ON dbo.order_items(order_id);
END;
COMMIT;
