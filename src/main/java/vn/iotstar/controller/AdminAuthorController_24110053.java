package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Author_24110053;
import vn.iotstar.services.LibraryServiceImpl_24110053;
import vn.iotstar.services.LibraryService_24110053;
import vn.iotstar.util.WebUtil_24110053;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/admin/authors/*")
public class AdminAuthorController_24110053 extends HttpServlet {
    private static final int PAGE_SIZE = 6;
    private final LibraryService_24110053 service = new LibraryServiceImpl_24110053();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = action(request);
        if ("new".equals(action) || "edit".equals(action)) {
            Author_24110053 author = "edit".equals(action)
                    ? service.author(WebUtil_24110053.intParam(request, "id", -1)).orElse(null)
                    : new Author_24110053();
            if (author == null) { response.sendError(404); return; }
            request.setAttribute("author", author);
            request.getRequestDispatcher("/WEB-INF/views/admin/authors/form.jsp").forward(request, response);
            return;
        }
        int page = Math.max(1, WebUtil_24110053.intParam(request, "page", 1));
        int totalPages = service.authorPages(PAGE_SIZE);
        page = Math.min(page, totalPages);
        request.setAttribute("authors", service.authors(page, PAGE_SIZE));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/WEB-INF/views/admin/authors/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if ("delete".equals(action(request))) {
                service.deleteAuthor(WebUtil_24110053.intParam(request, "id", -1));
                WebUtil_24110053.flash(request.getSession(), "success", "Đã xóa tác giả.");
            } else {
                Author_24110053 author = new Author_24110053();
                int id = WebUtil_24110053.intParam(request, "authorId", 0);
                author.setAuthorId(id == 0 ? null : id);
                author.setAuthorName(WebUtil_24110053.text(request, "authorName"));
                String birth = WebUtil_24110053.text(request, "dateOfBirth");
                author.setDateOfBirth(birth.isBlank() ? null : LocalDate.parse(birth));
                service.saveAuthor(author);
                WebUtil_24110053.flash(request.getSession(), "success", "Đã lưu thông tin tác giả.");
            }
        } catch (RuntimeException exception) {
            WebUtil_24110053.flash(request.getSession(), "danger", "Không thể xử lý: " + exception.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/admin/authors");
    }

    private static String action(HttpServletRequest request) {
        String path = request.getPathInfo();
        return path == null || path.length() <= 1 ? "list" : path.substring(1);
    }
}
