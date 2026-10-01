<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html><head><title>${empty book.bookid ? 'Thêm sách' : 'Sửa sách'}</title></head><body>
<c:set var="selectedAuthorId" value="0"/><c:forEach items="${book.authors}" var="assigned"><c:set var="selectedAuthorId" value="${assigned.authorId}"/></c:forEach>
<section class="form-card"><h1>${empty book.bookid ? 'Thêm sách mới' : 'Cập nhật sách'}</h1>
<form action="${pageContext.request.contextPath}/admin/books/save" method="post">
    <input type="hidden" name="bookid" value="${book.bookid}">
    <div class="field"><label for="isbn">ISBN</label><input id="isbn" name="isbn" type="number" value="${book.isbn}" required></div>
    <div class="field"><label for="title">Tên sách</label><input id="title" name="title" maxlength="200" value="${book.title}" required></div>
    <div class="field"><label for="authorId">Tác giả</label><select id="authorId" name="authorId" required><option value="">-- Chọn tác giả --</option><c:forEach items="${authors}" var="author"><option value="${author.authorId}" ${selectedAuthorId == author.authorId ? 'selected' : ''}>${author.authorName}</option></c:forEach></select></div>
    <div class="field"><label for="publisher">Nhà xuất bản</label><input id="publisher" name="publisher" maxlength="100" value="${book.publisher}" required></div>
    <div class="field"><label for="price">Giá</label><input id="price" name="price" type="number" step="0.01" min="0" value="${book.price}" required></div>
    <div class="field"><label for="publishDate">Ngày xuất bản</label><input id="publishDate" name="publishDate" type="date" value="${book.publishDate}" required></div>
    <div class="field"><label for="coverImage">Tên file ảnh bìa</label><input id="coverImage" name="coverImage" maxlength="100" value="${book.coverImage}" placeholder="ten-anh.jpg" required></div>
    <div class="field"><label for="quantity">Số lượng</label><input id="quantity" name="quantity" type="number" min="0" value="${book.quantity}" required></div>
    <div class="field"><label for="description">Mô tả</label><textarea id="description" name="description">${book.description}</textarea></div>
    <div class="form-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/books">Quay lại</a><button class="btn" type="submit">Lưu sách</button></div>
</form></section></body></html>
