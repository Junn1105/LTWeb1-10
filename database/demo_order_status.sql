USE [BaiTapGiuaKy];
-- Xem ma don vua dat va chu so huu truoc khi cap nhat.
SELECT id, user_id, recipient, created_at, status, total
FROM dbo.customer_orders ORDER BY id DESC;

-- Thay NULL bang ma don muon thu, chon mot ma trang thai ben duoi.
DECLARE @OrderId INT = NULL;
DECLARE @Status VARCHAR(20) = 'CONFIRMED';
-- NEW        : Don hang moi
-- CONFIRMED  : Da xac nhan
-- PREPARING  : Chuan bi hang
-- SHIPPING   : Van chuyen
-- DELIVERING : Giao hang
-- DELIVERED  : Da giao
-- CANCELLED  : Don hang huy
-- RETURNED   : Don hang hoan
IF @OrderId IS NOT NULL
BEGIN
    UPDATE dbo.customer_orders SET status = @Status WHERE id = @OrderId;
    SELECT id, status, total FROM dbo.customer_orders WHERE id = @OrderId;
END;
-- Tai lai /orders hoac bam Loc / Lam moi de quan sat.
-- Script chi doi trang thai de demo, khong tu dong hoan ton kho.
