USE [BaiTapGiuaKy];
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    /* 5 tai khoan: 1 Admin va 4 User. Mat khau theo yeu cau de bai. */
    IF EXISTS (SELECT 1 FROM dbo.users WHERE email = 'sang.nguyen@example.com')
       AND NOT EXISTS (SELECT 1 FROM dbo.users WHERE email = 'nguyenphuocsang1105@gmail.com')
        UPDATE dbo.users
        SET email = 'nguyenphuocsang1105@gmail.com'
        WHERE email = 'sang.nguyen@example.com';

    IF NOT EXISTS (SELECT 1 FROM dbo.users WHERE email = 'nguyenphuocsang1105@gmail.com')
        INSERT dbo.users (email, fullname, phone, passwd, signup_date, last_login, is_admin)
        VALUES ('nguyenphuocsang1105@gmail.com', N'Nguyễn Phước Sang', 901234561, '123456', '2026-09-01T08:00:00', '2026-09-24T08:15:00', 1);

    /* Gmail alias van nhan thu trong hop nguyenphuocsang1105@gmail.com, nhung dang nhap khong bi trung email Admin. */
    IF NOT EXISTS (SELECT 1 FROM dbo.users WHERE email = 'nguyenphuocsang1105+user@gmail.com')
        INSERT dbo.users (email, fullname, phone, passwd, signup_date, last_login, is_admin)
        VALUES ('nguyenphuocsang1105+user@gmail.com', N'Nguyễn Phước Sang - User Test', 901234566, '123456', '2026-09-24T14:30:00', NULL, 0);

    IF NOT EXISTS (SELECT 1 FROM dbo.users WHERE email = 'hoang.le@example.com')
        INSERT dbo.users (email, fullname, phone, passwd, signup_date, last_login, is_admin)
        VALUES ('hoang.le@example.com', N'Lê Thu Hoàng', 901234562, '123456', '2026-09-02T09:00:00', '2026-09-23T19:30:00', 0);

    IF NOT EXISTS (SELECT 1 FROM dbo.users WHERE email = 'khang.doan@example.com')
        INSERT dbo.users (email, fullname, phone, passwd, signup_date, last_login, is_admin)
        VALUES ('khang.doan@example.com', N'Đoàn Phúc Khang', 901234563, '123456', '2026-09-03T10:00:00', NULL, 0);

    IF NOT EXISTS (SELECT 1 FROM dbo.users WHERE email = 'khang.nguyen@example.com')
        INSERT dbo.users (email, fullname, phone, passwd, signup_date, last_login, is_admin)
        VALUES ('khang.nguyen@example.com', N'Nguyễn Minh Khang', 901234564, '123456', '2026-09-04T11:00:00', '2026-09-24T07:45:00', 0);

    IF NOT EXISTS (SELECT 1 FROM dbo.users WHERE email = 'nga.nguyen@example.com')
        INSERT dbo.users (email, fullname, phone, passwd, signup_date, last_login, is_admin)
        VALUES ('nga.nguyen@example.com', N'Nguyễn Thị Thùy Nga', 901234565, '123456', '2026-09-05T12:00:00', NULL, 0);

    /* Tac gia: ten khong dau de phu hop kieu VARCHAR cua de. */
    IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'To Hoai')
        INSERT dbo.author (author_name, date_of_birth) VALUES ('To Hoai', '1920-09-27');
    IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'Nguyen Nhat Anh')
        INSERT dbo.author (author_name, date_of_birth) VALUES ('Nguyen Nhat Anh', '1955-05-07');
    IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'Vu Trong Phung')
        INSERT dbo.author (author_name, date_of_birth) VALUES ('Vu Trong Phung', '1912-10-20');
    IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'Ngo Tat To')
        INSERT dbo.author (author_name, date_of_birth) VALUES ('Ngo Tat To', '1893-01-01');
    IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'Ho Chi Minh')
        INSERT dbo.author (author_name, date_of_birth) VALUES ('Ho Chi Minh', '1890-05-19');
    IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'Gosho Aoyama')
        INSERT dbo.author (author_name, date_of_birth) VALUES ('Gosho Aoyama', '1963-06-21');
    IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'Fujiko F. Fujio')
        INSERT dbo.author (author_name, date_of_birth) VALUES ('Fujiko F. Fujio', '1933-12-01');
    IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'Makoto Shinkai')
        INSERT dbo.author (author_name, date_of_birth) VALUES ('Makoto Shinkai', '1973-02-09');
    IF NOT EXISTS (SELECT 1 FROM dbo.author WHERE author_name = 'Reki Kawahara')
        INSERT dbo.author (author_name, date_of_birth) VALUES ('Reki Kawahara', NULL);

    /* 10 sach, quantity lan luot 5+4+3+2+1+4+3+2+1+5 = 30. */
    IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100000001)
        INSERT dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
        VALUES (100000001, 'De Men Phieu Luu Ky', 'Kim Dong', 55.00, 'Tac pham thieu nhi kinh dien ve hanh trinh truong thanh cua De Men.', '2020-01-15', 'de-men-phieu-luu-ky.jpg', 5);
    IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100000002)
        INSERT dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
        VALUES (100000002, 'Cho Toi Xin Mot Ve Di Tuoi Tho', 'Tre', 85.00, 'Cau chuyen trong treo va hai huoc ve ky uc tuoi tho.', '2021-03-10', 'cho-toi-xin-mot-ve.jpg', 4);
    IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100000003)
        INSERT dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
        VALUES (100000003, 'Mat Biec', 'Tre', 95.00, 'Cau chuyen tinh yeu va ky uc lang que cua Nguyen Nhat Anh.', '2022-05-20', 'mat-biec.jpg', 3);
    IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100000004)
        INSERT dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
        VALUES (100000004, 'So Do', 'Van Hoc', 68.00, 'Tieu thuyet hien thuc trao phung noi tieng cua van hoc Viet Nam.', '2019-08-12', 'so-do.jpg', 2);
    IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100000005)
        INSERT dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
        VALUES (100000005, 'Tat Den', 'Van Hoc', 62.00, 'Tac pham hien thuc viet ve cuoc song nong dan Viet Nam.', '2018-09-05', 'tat-den.jpg', 1);
    IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100000006)
        INSERT dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
        VALUES (100000006, 'Nhat Ky Trong Tu', 'Chinh Tri Quoc Gia', 72.00, 'Tap tho duoc sang tac trong thoi gian tac gia bi giam giu.', '2020-05-19', 'nhat-ky-trong-tu.jpg', 4);
    IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100000007)
        INSERT dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
        VALUES (100000007, 'Tham Tu Lung Danh Conan - Tap 1', 'Kim Dong', 25.00, 'Tap dau cua bo truyen tranh trinh tham noi tieng.', '2023-02-01', 'conan-tap-1.jpg', 3);
    IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100000008)
        INSERT dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
        VALUES (100000008, 'Doraemon - Tap 1', 'Kim Dong', 22.00, 'Nhung cau chuyen dau tien ve chu meo may den tu tuong lai.', '2023-01-10', 'doraemon-tap-1.jpg', 2);
    IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100000009)
        INSERT dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
        VALUES (100000009, 'Your Name', 'IPM', 89.00, 'Light novel ve hai nguoi tre bi hoan doi than xac va ky uc.', '2021-07-07', 'your-name.jpg', 1);
    IF NOT EXISTS (SELECT 1 FROM dbo.books WHERE isbn = 100000010)
        INSERT dbo.books (isbn, title, publisher, price, description, publish_date, cover_image, quantity)
        VALUES (100000010, 'Sword Art Online - Tap 1', 'IPM', 120.00, 'Light novel phieu luu trong the gioi tro choi thuc te ao.', '2022-11-11', 'sword-art-online-1.jpg', 5);

    /* Lien ket moi sach voi tac gia tuong ung. */
    INSERT dbo.book_author (bookid, author_id)
    SELECT b.bookid, a.author_id
    FROM (VALUES
        (100000001, 'To Hoai'),
        (100000002, 'Nguyen Nhat Anh'),
        (100000003, 'Nguyen Nhat Anh'),
        (100000004, 'Vu Trong Phung'),
        (100000005, 'Ngo Tat To'),
        (100000006, 'Ho Chi Minh'),
        (100000007, 'Gosho Aoyama'),
        (100000008, 'Fujiko F. Fujio'),
        (100000009, 'Makoto Shinkai'),
        (100000010, 'Reki Kawahara')
    ) AS x(isbn, author_name)
    JOIN dbo.books b ON b.isbn = x.isbn
    JOIN dbo.author a ON a.author_name = x.author_name
    WHERE NOT EXISTS
    (
        SELECT 1 FROM dbo.book_author ba
        WHERE ba.bookid = b.bookid AND ba.author_id = a.author_id
    );

    /* Danh gia da dang: nhieu review cho mot sach, review rong va sach chua co review. */
    INSERT dbo.rating (userid, bookid, rating, review_text)
    SELECT u.id, b.bookid, x.rating_value, x.review_text
    FROM (VALUES
        ('nguyenphuocsang1105@gmail.com', 100000001, CAST(5 AS TINYINT), 'Noi dung gan gui, phu hop nhieu lua tuoi.'),
        ('hoang.le@example.com',    100000001, CAST(4 AS TINYINT), 'Sach hay va de doc.'),
        ('khang.doan@example.com',  100000002, CAST(5 AS TINYINT), 'Mot ve tuoi tho rat dang nho.'),
        ('khang.nguyen@example.com',100000002, CAST(4 AS TINYINT), NULL),
        ('nga.nguyen@example.com',  100000003, CAST(5 AS TINYINT), 'Cau chuyen cam dong.'),
        ('nguyenphuocsang1105@gmail.com', 100000004, CAST(5 AS TINYINT), 'Chat trao phung rat dac sac.'),
        ('hoang.le@example.com',    100000005, CAST(4 AS TINYINT), 'Tac pham co gia tri hien thuc.'),
        ('khang.doan@example.com',  100000006, CAST(5 AS TINYINT), 'Nhieu bai tho co y nghia.'),
        ('khang.nguyen@example.com',100000007, CAST(5 AS TINYINT), 'Mo dau hap dan.'),
        ('nga.nguyen@example.com',  100000007, CAST(4 AS TINYINT), 'Truyen trinh tham de theo doi.'),
        ('nguyenphuocsang1105@gmail.com', 100000008, CAST(4 AS TINYINT), 'Vui nhon va hoai niem.'),
        ('hoang.le@example.com',    100000009, CAST(5 AS TINYINT), 'Cau chuyen lang man, de doc.')
    ) AS x(email, isbn, rating_value, review_text)
    JOIN dbo.users u ON u.email = x.email
    JOIN dbo.books b ON b.isbn = x.isbn
    WHERE NOT EXISTS
    (
        SELECT 1 FROM dbo.rating r
        WHERE r.userid = u.id AND r.bookid = b.bookid
    );

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRANSACTION;
    THROW;
END CATCH;
