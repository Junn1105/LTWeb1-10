<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html><html><head><title>Quản lý tác giả</title></head><body>
<div class="page-heading"><div><h1>Quản lý tác giả</h1><p class="hint">Danh sách tác giả trong thư viện.</p></div><a class="btn" href="${pageContext.request.contextPath}/admin/authors/new">+ Thêm tác giả</a></div>
<table><thead><tr><th>Mã</th><th>Tên tác giả</th><th>Ngày sinh</th><th>Thao tác</th></tr></thead><tbody>
<c:forEach items="${authors}" var="author"><tr><td>${author.authorId}</td><td>${author.authorName}</td><td>${author.dateOfBirth}</td><td><div class="actions"><a class="btn btn-small" href="${pageContext.request.contextPath}/admin/authors/edit?id=${author.authorId}">Sửa</a><form action="${pageContext.request.contextPath}/admin/authors/delete" method="post" onsubmit="return confirm('Xóa tác giả này?')"><input type="hidden" name="id" value="${author.authorId}"><button class="btn btn-danger btn-small" type="submit">Xóa</button></form></div></td></tr></c:forEach>
</tbody></table>
<div class="pagination"><c:forEach begin="1" end="${totalPages}" var="p"><c:choose><c:when test="${p == currentPage}"><span class="active">${p}</span></c:when><c:otherwise><a href="${pageContext.request.contextPath}/admin/authors?page=${p}">${p}</a></c:otherwise></c:choose></c:forEach></div>
</body></html>
