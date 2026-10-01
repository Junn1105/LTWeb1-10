package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Book_24110053;
import vn.iotstar.services.LibraryServiceImpl_24110053;
import vn.iotstar.services.LibraryService_24110053;
import vn.iotstar.util.WebUtil_24110053;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@WebServlet("/admin/books/*")
public class AdminBookController_24110053 extends HttpServlet {
    private static final int PAGE_SIZE = 6;
    private final LibraryService_24110053 service = new LibraryServiceImpl_24110053();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = action(request);
        if ("new".equals(action) || "edit".equals(action)) {
            Book_24110053 book = "edit".equals(action)
                    ? service.book(WebUtil_24110053.intParam(request, "id", -1)).orElse(null)
                    : new Book_24110053();
            if (book == null) { response.sendError(404); return; }
            request.setAttribute("book", book);
            request.setAttribute("authors", service.allAuthors());
            request.getRequestDispatcher("/WEB-INF/views/admin/books/form.jsp").forward(request, response);
            return;
        }
        int page = Math.max(1, WebUtil_24110053.intParam(request, "page", 1));
        int totalPages = service.bookPages(PAGE_SIZE);
        page = Math.min(page, totalPages);
        request.setAttribute("books", service.books(page, PAGE_SIZE));
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/WEB-INF/views/admin/books/list.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if ("delete".equals(action(request))) {
                service.deleteBook(WebUtil_24110053.intParam(request, "id", -1));
                WebUtil_24110053.flash(request.getSession(), "success", "Đã xóa sách.");
            } else {
                Book_24110053 book = new Book_24110053();
                int id = WebUtil_24110053.intParam(request, "bookid", 0);
                book.setBookid(id == 0 ? null : id);
                book.setIsbn(Integer.valueOf(WebUtil_24110053.text(request, "isbn")));
                book.setTitle(WebUtil_24110053.text(request, "title"));
                book.setPublisher(WebUtil_24110053.text(request, "publisher"));
                book.setPrice(new BigDecimal(WebUtil_24110053.text(request, "price")));
                book.setDescription(WebUtil_24110053.text(request, "description"));
                book.setPublishDate(LocalDate.parse(WebUtil_24110053.text(request, "publishDate")));
                book.setCoverImage(WebUtil_24110053.text(request, "coverImage"));
                book.setQuantity(WebUtil_24110053.intParam(request, "quantity", 0));
                int authorId = WebUtil_24110053.intParam(request, "authorId", 0);
                service.saveBook(book, authorId == 0 ? null : authorId);
                WebUtil_24110053.flash(request.getSession(), "success", "Đã lưu thông tin sách.");
            }
        } catch (RuntimeException exception) {
            WebUtil_24110053.flash(request.getSession(), "danger", "Không thể xử lý: " + exception.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/admin/books");
    }

    private static String action(HttpServletRequest request) {
        String path = request.getPathInfo();
        return path == null || path.length() <= 1 ? "list" : path.substring(1);
    }
}
