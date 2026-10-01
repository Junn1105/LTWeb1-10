<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html><head><title>Xác thực OTP</title></head><body>
<section class="auth-card"><h1>Xác thực email</h1>
    <% if (request.getAttribute("error") != null) { %><div class="alert alert-danger"><%= request.getAttribute("error") %></div><% } %>
    <p>Mã OTP đã được gửi tới email đăng ký và có hiệu lực trong 5 phút.</p>
    <c:if test="${not empty sessionScope.developmentOtp}"><p class="hint">Chế độ phát triển: OTP là <strong>${sessionScope.developmentOtp}</strong>. Cấu hình SMTP để gửi email thật.</p></c:if>
    <form method="post" action="${pageContext.request.contextPath}/verify-otp">
        <div class="field"><label for="otp">Mã OTP</label><input id="otp" name="otp" inputmode="numeric" minlength="6" maxlength="6" required></div>
        <button class="btn" type="submit">Xác nhận đăng ký</button>
    </form>
</section></body></html>
