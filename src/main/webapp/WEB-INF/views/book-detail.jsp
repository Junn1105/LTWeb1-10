<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html><html><head><title>${book.title}</title></head><body>
<article class="detail">
    <img class="detail-cover" src="${pageContext.request.contextPath}/assets/images/${book.coverImage}" alt="Bìa ${book.title}">
    <div>
        <h1>${book.title}</h1>
        <p class="stars">★ <fmt:formatNumber value="${averageRating}" pattern="0.0"/>/5 (${ratings.size()} lượt)</p>
        <p><strong>ISBN:</strong> ${book.isbn}</p>
        <p><strong>Tác giả:</strong> <c:forEach items="${book.authors}" var="author" varStatus="status">${author.authorName}<c:if test="${!status.last}">, </c:if></c:forEach></p>
        <p><strong>Nhà xuất bản:</strong> ${book.publisher}</p>
        <p><strong>Ngày xuất bản:</strong> ${book.publishDate}</p>
        <p><strong>Số lượng:</strong> ${book.quantity}</p>
        <p class="price"><fmt:formatNumber value="${book.price}" pattern="#0.00"/> VNĐ</p>
        <p>${book.description}</p>
        <form class="quantity-form" method="post" action="${pageContext.request.contextPath}/cart">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"><input type="hidden" name="action" value="add"><input type="hidden" name="bookId" value="${book.bookid}">
            <label for="buy-quantity">Số lượng</label><input id="buy-quantity" type="number" name="quantity" min="1" max="${book.quantity gt 99 ? 99 : book.quantity}" value="1" required>
            <button class="btn" ${empty book.quantity or book.quantity lt 1 or empty book.price ? 'disabled' : ''}>${book.quantity gt 0 ? 'Thêm vào giỏ' : 'Hết hàng'}</button>
        </form>
    </div>
</article>

<section class="rating-box">
    <h2>Đánh giá sách</h2>
    <c:choose>
        <c:when test="${not empty sessionScope.currentUser}">
            <form action="${pageContext.request.contextPath}/review" method="post">
                <input type="hidden" name="bookId" value="${book.bookid}">
                <div class="field"><label for="rating">Số sao</label><select id="rating" name="rating"><option value="5">5 - Xuất sắc</option><option value="4">4 - Tốt</option><option value="3">3 - Khá</option><option value="2">2 - Trung bình</option><option value="1">1 - Chưa tốt</option></select></div>
                <div class="field"><label for="reviewText">Nhận xét</label><textarea id="reviewText" name="reviewText" maxlength="1000"></textarea></div>
                <button class="btn" type="submit">Gửi đánh giá</button>
            </form>
        </c:when>
        <c:otherwise><p>Vui lòng <a href="${pageContext.request.contextPath}/login">đăng nhập</a> để đánh giá.</p></c:otherwise>
    </c:choose>
</section>
<section>
    <h2>Nhận xét (${ratings.size()})</h2>
    <c:forEach items="${ratings}" var="item">
        <article class="review"><strong>${item.user.fullname}</strong> <span class="stars">${item.rating}/5 ★</span><c:if test="${not empty item.reviewText}"><p>${item.reviewText}</p></c:if></article>
    </c:forEach>
    <c:if test="${empty ratings}"><p>Chưa có đánh giá cho sách này.</p></c:if>
</section>
</body></html>
