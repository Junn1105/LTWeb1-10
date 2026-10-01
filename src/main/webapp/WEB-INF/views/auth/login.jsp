<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html><html><head><title>Đăng nhập</title></head><body>
<section class="auth-card"><h1>Đăng nhập</h1>
    <% if (request.getAttribute("error") != null) { %><div class="alert alert-danger"><%= request.getAttribute("error") %></div><% } %>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="field"><label for="email">Email</label><input id="email" name="email" type="email" value="${email}" required></div>
        <div class="field"><label for="password">Mật khẩu</label><input id="password" name="password" type="password" required></div>
        <button class="btn" type="submit">Đăng nhập</button>
    </form>
    <p>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký</a></p>
</section></body></html>
