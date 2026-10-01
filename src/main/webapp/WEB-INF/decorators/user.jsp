<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title"/> | Thư viện 24110053</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <sitemesh:write property="head"/>
</head>
<body>
<header class="site-header">
    <div class="container nav-wrap">
        <a class="brand" href="${pageContext.request.contextPath}/home">SÁCH VIỆT</a>
        <nav>
            <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
            <a href="${pageContext.request.contextPath}/products">Sản phẩm</a>
            <a href="${pageContext.request.contextPath}/cart">Giỏ hàng</a>
            <a href="${pageContext.request.contextPath}/orders">Lịch sử đặt hàng</a>
            <c:choose>
                <c:when test="${empty sessionScope.currentUser}">
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                </c:when>
                <c:otherwise>
                    <span class="hello">Xin chào, ${sessionScope.currentUser.fullname}</span>
                    <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                </c:otherwise>
            </c:choose>
            <c:if test="${sessionScope.currentUser.admin}">
                <a class="admin-link" href="${pageContext.request.contextPath}/admin/books">Trang quản trị</a>
            </c:if>
        </nav>
    </div>
</header>
<main class="container main-content">
    <c:if test="${not empty sessionScope.flashMessage}">
        <div class="alert alert-${sessionScope.flashType}">${sessionScope.flashMessage}</div>
        <c:remove var="flashMessage" scope="session"/>
        <c:remove var="flashType" scope="session"/>
    </c:if>
    <sitemesh:write property="body"/>
</main>
<footer class="site-footer">
    <div class="container">Nguyễn Phước Sang &nbsp;|&nbsp; MSSV: 24110053 &nbsp;|&nbsp; Mã đề: 01</div>
</footer>
</body>
</html>
