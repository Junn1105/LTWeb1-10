<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title"/> | Quản trị</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <sitemesh:write property="head"/>
</head>
<body class="admin-body">
<header class="site-header admin-header">
    <div class="container nav-wrap">
        <a class="brand" href="${pageContext.request.contextPath}/admin/books">QUẢN TRỊ THƯ VIỆN</a>
        <nav>
            <a href="${pageContext.request.contextPath}/admin/books">Quản lý sách</a>
            <a href="${pageContext.request.contextPath}/admin/authors">Quản lý tác giả</a>
            <a href="${pageContext.request.contextPath}/home">Trang người dùng</a>
            <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
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
