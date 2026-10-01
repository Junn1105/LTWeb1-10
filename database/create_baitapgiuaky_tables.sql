USE [BaiTapGiuaKy];
SET NOCOUNT ON;
SET XACT_ABORT ON;

BEGIN TRY
    BEGIN TRANSACTION;

    IF OBJECT_ID(N'dbo.books', N'U') IS NULL
    BEGIN
        CREATE TABLE dbo.books
        (
            bookid       INT IDENTITY(1,1) NOT NULL,
            isbn         INT NULL,
            title        VARCHAR(200) NULL,
            publisher    VARCHAR(100) NULL,
            price        DECIMAL(6,2) NULL,
            description  TEXT NULL,
            publish_date DATE NULL,
            cover_image  VARCHAR(100) NULL,
            quantity     INT NULL,
            CONSTRAINT PK_books PRIMARY KEY (bookid)
        );
    END;

    IF OBJECT_ID(N'dbo.users', N'U') IS NULL
    BEGIN
        CREATE TABLE dbo.users
        (
            id          INT IDENTITY(1,1) NOT NULL,
            email       VARCHAR(50) NOT NULL,
            fullname    NVARCHAR(50) NULL,
            phone       INT NULL,
            passwd      VARCHAR(32) NOT NULL,
            signup_date DATETIME NULL,
            last_login  DATETIME NULL,
            is_admin    BIT NULL,
            CONSTRAINT PK_users PRIMARY KEY (id)
        );
    END;

    IF OBJECT_ID(N'dbo.author', N'U') IS NULL
    BEGIN
        CREATE TABLE dbo.author
        (
            author_id    INT IDENTITY(1,1) NOT NULL,
            author_name  VARCHAR(100) NULL,
            date_of_birth DATE NULL,
            CONSTRAINT PK_author PRIMARY KEY (author_id)
        );
    END;

    IF OBJECT_ID(N'dbo.book_author', N'U') IS NULL
    BEGIN
        CREATE TABLE dbo.book_author
        (
            bookid   INT NOT NULL,
            author_id INT NOT NULL,
            CONSTRAINT PK_book_author PRIMARY KEY (bookid, author_id),
            CONSTRAINT FK_book_author_books
                FOREIGN KEY (bookid) REFERENCES dbo.books(bookid),
            CONSTRAINT FK_book_author_author
                FOREIGN KEY (author_id) REFERENCES dbo.author(author_id)
        );
    END;

    IF OBJECT_ID(N'dbo.rating', N'U') IS NULL
    BEGIN
        CREATE TABLE dbo.rating
        (
            userid      INT NOT NULL,
            bookid      INT NOT NULL,
            rating      TINYINT NULL,
            review_text TEXT NULL,
            CONSTRAINT PK_rating PRIMARY KEY (userid, bookid),
            CONSTRAINT FK_rating_users
                FOREIGN KEY (userid) REFERENCES dbo.users(id),
            CONSTRAINT FK_rating_books
                FOREIGN KEY (bookid) REFERENCES dbo.books(bookid)
        );
    END;

    COMMIT TRANSACTION;
END TRY
BEGIN CATCH
    IF @@TRANCOUNT > 0
        ROLLBACK TRANSACTION;
    THROW;
END CATCH;
