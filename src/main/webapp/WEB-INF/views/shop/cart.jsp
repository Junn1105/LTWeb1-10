<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html><head><title>Giỏ hàng</title></head><body>
<div class="page-heading"><h1>Giỏ hàng</h1><a href="${pageContext.request.contextPath}/products">Tiếp tục mua sách</a></div>
<p class="hint">Mỗi đầu sách tối đa 99 cuốn và không vượt tồn kho. Giỏ hàng được giữ trong phiên đăng nhập.</p>
<c:choose><c:when test="${empty lines}">
    <div class="form-card"><h2>Giỏ hàng đang trống</h2><p>Hãy chọn sách để bắt đầu đặt hàng.</p><a class="btn" href="${pageContext.request.contextPath}/products">Khám phá sách</a></div>
</c:when><c:otherwise>
<div class="table-scroll"><table><thead><tr><th>Sách</th><th>Đơn giá</th><th>Số lượng</th><th>Thành tiền</th><th>Thao tác</th></tr></thead><tbody>
<c:forEach items="${lines}" var="line"><tr>
    <td><c:out value="${line.title}"/><p class="hint">Còn ${line.stock} cuốn</p><c:if test="${!line.available}"><p class="stock-error">Sách không đủ tồn kho hoặc không còn bán. Hãy giảm số lượng hoặc xóa.</p></c:if></td>
    <td><fmt:formatNumber value="${line.price}" pattern="#,##0.00"/> VNĐ</td>
    <td><form class="quantity-form" method="post" action="${pageContext.request.contextPath}/cart">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><input type="hidden" name="action" value="update"><input type="hidden" name="bookId" value="${line.bookId}">
        <label class="sr-only" for="qty-${line.bookId}">Số lượng sách</label>
        <input id="qty-${line.bookId}" type="number" name="quantity" min="1" max="${line.limit}" value="${line.quantity}" required>
        <button class="btn btn-small" ${line.limit lt 1 ? 'disabled' : ''}>Cập nhật</button>
    </form></td>
    <td><fmt:formatNumber value="${line.subtotal}" pattern="#,##0.00"/> VNĐ</td>
    <td><form method="post" action="${pageContext.request.contextPath}/cart"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><input type="hidden" name="action" value="remove"><input type="hidden" name="bookId" value="${line.bookId}"><button class="btn btn-danger btn-small">Xóa</button></form></td>
</tr></c:forEach></tbody></table></div>
<div class="form-card"><p class="price">Tổng cộng: <fmt:formatNumber value="${total}" pattern="#,##0.00"/> VNĐ</p><div class="form-actions">
    <form method="post" action="${pageContext.request.contextPath}/cart"><input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><input type="hidden" name="action" value="clear"><button class="btn btn-secondary">Xóa toàn bộ giỏ</button></form>
    <c:choose><c:when test="${ready}"><a class="btn" href="${pageContext.request.contextPath}/checkout">Thanh toán COD</a></c:when><c:otherwise><span class="stock-error">Cập nhật giỏ hàng để tiếp tục thanh toán.</span></c:otherwise></c:choose>
</div></div>
</c:otherwise></c:choose>
</body></html>
