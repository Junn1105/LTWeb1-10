# BT Giữa Kỳ - Nguyễn Phước Sang - 24110053

Ứng dụng quản lý thư viện sử dụng Servlet, JSP, JPA/Hibernate, SQL Server và SiteMesh.

## Chuẩn bị dữ liệu

1. Mở SQL Server Management Studio và tạo database `BaiTapGiuaKy`.
2. Chạy lần lượt `database/create_baitapgiuaky_tables.sql`, `database/seed_baitapgiuaky_data.sql` và `database/add_shopping_tables.sql`. Với database đã có dữ liệu, chỉ cần chạy `add_shopping_tables.sql` để bổ sung bảng mua hàng; không chạy lại seed.
3. Mặc định ứng dụng kết nối SQL Server tại `localhost:1433` bằng Windows Authentication. Có thể ghi đè bằng biến môi trường:
   - `DB_URL`
   - `DB_USER`
   - `DB_PASSWORD`

## Gửi OTP email thật

Cấu hình các biến môi trường `SMTP_HOST`, `SMTP_PORT`, `SMTP_USER`, `SMTP_PASSWORD`, `SMTP_FROM`. Nếu chưa cấu hình SMTP, mã OTP được hiển thị ở màn hình xác thực để có thể kiểm thử đầy đủ luồng đăng ký.

## Tài khoản có sẵn

- Admin: `nguyenphuocsang1105@gmail.com` / `123456`
- User kiểm thử: `nguyenphuocsang1105+user@gmail.com` / `123456`
- User: `hoang.le@example.com` / `123456`

## Chạy project

### Cấu hình IntelliJ đã chuẩn bị

Chọn cấu hình **BTGiuaKy - User** ở thanh Run. Nếu chưa thấy, mở lại project để IntelliJ nạp `.run/BTGiuaKy_User.run.xml`. Cấu hình dùng artifact **BTGiuaKy:war exploded**, context `/BTGiuaKy` và DLL xác thực SQL Server. Sau khi sửa mã nguồn, chạy `mvn package` trước khi Run để cập nhật nội dung `target/BTGiuaKy` mà artifact sử dụng. Dừng Tomcat đang chiếm cổng 8080 trước khi chạy một instance mới.

Không sử dụng artifact `unnamed` rỗng. Tomcat do IntelliJ chạy có `CATALINA_BASE` riêng và có thể bỏ qua WAR chép vào thư mục `webapps` của bộ cài; cần khai báo artifact trong Deployment như cấu hình trên.

Project WAR không có file `main()` để chạy trực tiếp. Trong IntelliJ IDEA:

1. Chọn **Run > Edit Configurations > + > Tomcat Server > Local**.
2. Chọn Application Server: `D:\LT Web\apache-tomcat-10.1.44\apache-tomcat-10.1.44`.
3. Tại **Deployment**, thêm artifact `BTGiuaKy:war exploded`.
4. Tại **VM options**, thêm `-Djava.library.path="D:\LT Web\sqlserver-auth"` để Windows Authentication kết nối SQL Server.
5. Chạy cấu hình Tomcat và mở `http://localhost:8080/BTGiuaKy/home`.

Có thể đóng gói thủ công bằng `mvn clean package`; file triển khai nằm tại `target/BTGiuaKy.war`.

Nếu không có Tomcat Server, chạy `run-tomcat.bat` ở thư mục gốc project. Dùng `stop-tomcat.bat` để dừng máy chủ.

## Chức năng mua hàng của User

- Đăng nhập bằng tài khoản User, chọn **Thêm vào giỏ** ở trang sản phẩm hoặc chi tiết sách.
- **Giỏ hàng** (`/cart`): thêm, sửa số lượng, xóa từng sách hoặc xóa toàn bộ. Mỗi đầu sách giới hạn từ 1 đến `min(tồn kho, 99)`. Giỏ thuộc từng tài khoản trong phiên; đăng xuất/hết phiên sẽ mất giỏ.
- **Thanh toán COD** (`/checkout`): nhập người nhận, số điện thoại Việt Nam 10 chữ số bắt đầu bằng 0, địa chỉ, ghi chú tùy chọn. Phí vận chuyển hiện là 0 VNĐ. Giá và tồn kho được đọc lại từ database; khi giá thay đổi cần xác nhận lại. Đặt hàng và trừ tồn kho trong một transaction, lỗi sẽ rollback; thành công mới xóa giỏ.
- **Lịch sử đặt hàng** (`/orders`): chỉ xem đơn của tài khoản hiện tại; xem sách, giá tại lúc mua, người nhận và lọc đủ 8 trạng thái. COD là phương thức thu tiền khi nhận hàng, không phải thanh toán trực tuyến.

### Thử thay đổi trạng thái trực tiếp trong database

1. Đặt một đơn COD rồi ghi lại mã đơn ở trang lịch sử.
2. Mở `database/demo_order_status.sql` trong SQL Server Management Studio. Thay `@OrderId = NULL` bằng mã đơn và đặt `@Status` theo bảng dưới.
3. Chạy script, quay lại **Lịch sử đặt hàng**, chọn trạng thái và bấm **Lọc / Làm mới**. Đơn chuyển sang bộ lọc tương ứng ngay lần tải trang tiếp theo.

| Mã database | Hiển thị |
| --- | --- |
| NEW | Đơn hàng mới |
| CONFIRMED | Đã xác nhận |
| PREPARING | Chuẩn bị hàng |
| SHIPPING | Vận chuyển |
| DELIVERING | Giao hàng |
| DELIVERED | Đã giao |
| CANCELLED | Đơn hàng hủy |
| RETURNED | Đơn hàng hoàn |

Script demo chỉ thay đổi trạng thái, không hoàn tồn kho tự động khi hủy/hoàn. Luồng xử lý kho cho hủy/hoàn và chức năng quản trị đơn hàng chưa thuộc phạm vi này.

### Kiểm thử

`mvn test` chạy kiểm thử giới hạn giỏ và thông tin nhận hàng. Kiểm thử SQL Server được bật riêng, tạo dữ liệu tạm và dọn sau khi chạy:

```powershell
$env:SHOPPING_DB_TEST = 'true'
$env:PATH = 'D:\LT Web\sqlserver-auth;' + $env:PATH
mvn test
Remove-Item Env:SHOPPING_DB_TEST
```

Kiểm thử database bao gồm COD, rollback, lưu giá lúc mua, lọc cả 8 trạng thái và hai người đặt đồng thời cuốn cuối cùng. Cần chạy migration trước và cấu hình kết nối như trên.
