<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html><head><title>Thanh toán COD</title></head><body>
<h1>Thanh toán đơn hàng</h1>
<c:if test="${not empty error}"><div class="alert alert-danger" role="alert"><c:out value="${error}"/></div></c:if>
<c:choose><c:when test="${ready}"><div class="checkout-grid">
<form class="form-card" method="post" action="${pageContext.request.contextPath}/checkout">
    <h2>Thông tin nhận hàng</h2>
    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><input type="hidden" name="checkoutToken" value="${checkoutToken}">
    <div class="field"><label for="recipient">Họ tên người nhận</label><input id="recipient" name="recipient" maxlength="100" required autocomplete="name" value="<c:out value='${submitted ? param.recipient : sessionScope.currentUser.fullname}'/>"></div>
    <div class="field"><label for="phone">Số điện thoại</label><input id="phone" type="tel" name="phone" maxlength="10" pattern="0[0-9]{9}" required autocomplete="tel" placeholder="0901234567" value="<c:out value='${param.phone}'/>"></div>
    <div class="field"><label for="address">Địa chỉ nhận hàng</label><textarea id="address" name="address" maxlength="500" required autocomplete="street-address"><c:out value="${param.address}"/></textarea></div>
    <div class="field"><label for="note">Ghi chú (không bắt buộc)</label><textarea id="note" name="note" maxlength="500"><c:out value="${param.note}"/></textarea></div>
    <p><strong>Thanh toán tiền mặt khi nhận hàng (COD)</strong></p><p class="hint">Phí vận chuyển: 0 VNĐ. Bạn thanh toán tổng tiền bên dưới khi nhận sách.</p>
    <button class="btn" type="submit">Xác nhận đặt hàng COD</button>
</form>
<aside class="form-card"><h2>Đơn hàng của bạn</h2>
    <c:forEach items="${lines}" var="line"><div class="order-line"><strong><c:out value="${line.title}"/></strong><p>${line.quantity} × <fmt:formatNumber value="${line.price}" pattern="#,##0.00"/> VNĐ</p><p><fmt:formatNumber value="${line.subtotal}" pattern="#,##0.00"/> VNĐ</p></div></c:forEach>
    <p class="price">Tổng thanh toán: <fmt:formatNumber value="${total}" pattern="#,##0.00"/> VNĐ</p><a href="${pageContext.request.contextPath}/cart">Chỉnh sửa giỏ hàng</a>
</aside></div></c:when><c:otherwise><div class="form-card"><p>Giỏ hàng trống hoặc có sách không đủ điều kiện đặt mua.</p><a class="btn" href="${pageContext.request.contextPath}/cart">Kiểm tra giỏ hàng</a><a class="btn btn-secondary" href="${pageContext.request.contextPath}/orders">Lịch sử đặt hàng</a></div></c:otherwise></c:choose>
</body></html>
