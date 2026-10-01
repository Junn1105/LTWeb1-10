<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html><head><title>Lịch sử đặt hàng</title></head><body>
<h1>Lịch sử đặt hàng</h1>
<form class="order-filter" method="get" action="${pageContext.request.contextPath}/orders">
    <label for="status">Trạng thái đơn hàng</label><select id="status" name="status"><option value="">Tất cả trạng thái</option><c:forEach items="${statuses}" var="status"><option value="${status.code}" ${selectedStatus eq status.code ? 'selected' : ''}>${status.label}</option></c:forEach></select><button class="btn">Lọc / Làm mới</button>
</form>
<c:if test="${empty orders}"><div class="form-card"><p>Chưa có đơn hàng ở trạng thái này.</p><a href="${pageContext.request.contextPath}/products">Tiếp tục mua sách</a></div></c:if>
<c:forEach items="${orders}" var="order"><article class="form-card">
    <div class="page-heading"><h2>Đơn hàng #${order.id}</h2><span class="order-status status-${order.status.code}">${order.status.label}</span></div>
    <p class="hint">Đặt ngày ${order.createdAtText} · Thanh toán COD khi nhận hàng</p>
    <p><strong>Người nhận:</strong> <c:out value="${order.recipient}"/> · <c:out value="${order.phone}"/></p>
    <p><strong>Địa chỉ:</strong> <c:out value="${order.address}"/></p>
    <c:if test="${not empty order.note}"><p><strong>Ghi chú:</strong> <c:out value="${order.note}"/></p></c:if>
    <div class="table-scroll"><table><thead><tr><th>Sách</th><th>Đơn giá lúc đặt</th><th>Số lượng</th><th>Thành tiền</th></tr></thead><tbody>
    <c:forEach items="${order.items}" var="item"><tr><td><c:out value="${item.title}"/></td><td><fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/> VNĐ</td><td>${item.quantity}</td><td><fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/> VNĐ</td></tr></c:forEach>
    </tbody></table></div><p class="price">Tổng tiền: <fmt:formatNumber value="${order.total}" pattern="#,##0.00"/> VNĐ</p>
</article></c:forEach>
</body></html>
