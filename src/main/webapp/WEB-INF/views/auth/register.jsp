<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html><html><head><title>Đăng ký</title></head><body>
<section class="auth-card"><h1>Tạo tài khoản</h1>
    <% if (request.getAttribute("error") != null) { %><div class="alert alert-danger"><%= request.getAttribute("error") %></div><% } %>
    <form method="post" action="${pageContext.request.contextPath}/register">
        <div class="field"><label for="fullname">Họ và tên</label><input id="fullname" name="fullname" maxlength="50" required></div>
        <div class="field"><label for="email">Email</label><input id="email" name="email" type="email" maxlength="50" required></div>
        <div class="field"><label for="phone">Số điện thoại</label><input id="phone" name="phone" type="number"></div>
        <div class="field"><label for="password">Mật khẩu</label><input id="password" name="password" type="password" minlength="6" maxlength="32" required></div>
        <div class="field"><label for="confirmPassword">Nhập lại mật khẩu</label><input id="confirmPassword" name="confirmPassword" type="password" minlength="6" required></div>
        <button class="btn" type="submit">Gửi mã OTP</button>
    </form>
</section></body></html>
