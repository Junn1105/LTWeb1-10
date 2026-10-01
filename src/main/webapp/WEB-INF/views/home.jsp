<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html><head><title>Trang chủ</title></head><body>
<section class="hero">
    <h1>Thư viện sách Việt</h1>
    <p>Khám phá sách văn học, truyện tranh và light novel được yêu thích.</p>
</section>
<div class="book-grid">
    <c:forEach items="${books}" var="book">
        <article class="book-card">
            <div class="cover-wrap"><img src="${pageContext.request.contextPath}/assets/images/${book.coverImage}" alt="Bìa ${book.title}"></div>
            <div class="book-info">
                <h2>${book.title}</h2>
                <p class="meta"><strong>ISBN:</strong> ${book.isbn}</p>
                <p class="meta"><strong>Tác giả:</strong>
                    <c:forEach items="${book.authors}" var="author" varStatus="status">${author.authorName}<c:if test="${!status.last}">, </c:if></c:forEach>
                </p>
                <p class="meta"><strong>NXB:</strong> ${book.publisher} · ${book.publishDate}</p>
                <p class="meta"><strong>Số lượng:</strong> ${book.quantity} · <strong>Đánh giá:</strong> ${reviewCounts[book.bookid]}</p>
                <p class="price"><fmt:formatNumber value="${book.price}" pattern="#0.00"/> VNĐ</p>
                <a class="btn" href="${pageContext.request.contextPath}/book?id=${book.bookid}">Xem chi tiết</a>
                <form class="buy-form" method="post" action="${pageContext.request.contextPath}/cart">
                    <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><input type="hidden" name="action" value="add"><input type="hidden" name="bookId" value="${book.bookid}"><input type="hidden" name="quantity" value="1">
                    <button class="btn btn-secondary" ${empty book.quantity or book.quantity lt 1 or empty book.price ? 'disabled' : ''}>${book.quantity gt 0 ? 'Thêm vào giỏ' : 'Hết hàng'}</button>
                </form>
            </div>
        </article>
    </c:forEach>
</div>
<c:if test="${empty books}"><p>Chưa có sách trong thư viện.</p></c:if>
<div class="pagination">
    <c:forEach begin="1" end="${totalPages}" var="p">
        <c:choose><c:when test="${p == currentPage}"><span class="active">${p}</span></c:when>
        <c:otherwise><a href="${pageContext.request.contextPath}/home?page=${p}">${p}</a></c:otherwise></c:choose>
    </c:forEach>
</div>
</body></html>
