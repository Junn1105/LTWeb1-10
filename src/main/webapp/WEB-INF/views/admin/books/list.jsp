<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html><head><title>Quản lý sách</title></head><body>
<div class="page-heading"><div><h1>Quản lý sách</h1><p class="hint">Thêm, sửa, xóa và phân trang danh mục sách.</p></div><a class="btn" href="${pageContext.request.contextPath}/admin/books/new">+ Thêm sách</a></div>
<table><thead><tr><th>Bìa</th><th>ISBN</th><th>Tên sách</th><th>Tác giả</th><th>Số lượng</th><th>Thao tác</th></tr></thead><tbody>
<c:forEach items="${books}" var="book"><tr>
    <td><img class="thumb" src="${pageContext.request.contextPath}/assets/images/${book.coverImage}" alt=""></td>
    <td>${book.isbn}</td><td>${book.title}</td>
    <td><c:forEach items="${book.authors}" var="author" varStatus="status">${author.authorName}<c:if test="${!status.last}">, </c:if></c:forEach></td>
    <td>${book.quantity}</td><td><div class="actions"><a class="btn btn-small" href="${pageContext.request.contextPath}/admin/books/edit?id=${book.bookid}">Sửa</a><form action="${pageContext.request.contextPath}/admin/books/delete" method="post" onsubmit="return confirm('Xóa sách này?')"><input type="hidden" name="id" value="${book.bookid}"><button class="btn btn-danger btn-small" type="submit">Xóa</button></form></div></td>
</tr></c:forEach>
</tbody></table>
<div class="pagination"><c:forEach begin="1" end="${totalPages}" var="p"><c:choose><c:when test="${p == currentPage}"><span class="active">${p}</span></c:when><c:otherwise><a href="${pageContext.request.contextPath}/admin/books?page=${p}">${p}</a></c:otherwise></c:choose></c:forEach></div>
</body></html>
