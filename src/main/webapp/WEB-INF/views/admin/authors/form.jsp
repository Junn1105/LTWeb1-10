<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html><html><head><title>${empty author.authorId ? 'Thêm tác giả' : 'Sửa tác giả'}</title></head><body>
<section class="form-card"><h1>${empty author.authorId ? 'Thêm tác giả mới' : 'Cập nhật tác giả'}</h1>
<form action="${pageContext.request.contextPath}/admin/authors/save" method="post">
    <input type="hidden" name="authorId" value="${author.authorId}">
    <div class="field"><label for="authorName">Tên tác giả</label><input id="authorName" name="authorName" maxlength="100" value="${author.authorName}" required></div>
    <div class="field"><label for="dateOfBirth">Ngày sinh</label><input id="dateOfBirth" name="dateOfBirth" type="date" value="${author.dateOfBirth}"></div>
    <div class="form-actions"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/authors">Quay lại</a><button class="btn" type="submit">Lưu tác giả</button></div>
</form></section></body></html>
