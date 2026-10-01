package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.Book_24110053;
import vn.iotstar.entity.Rating_24110053;
import vn.iotstar.services.LibraryServiceImpl_24110053;
import vn.iotstar.services.LibraryService_24110053;
import vn.iotstar.util.WebUtil_24110053;

import java.io.IOException;
import java.util.List;

@WebServlet("/book")
public class BookDetailController_24110053 extends HttpServlet {
    private final LibraryService_24110053 service = new LibraryServiceImpl_24110053();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = WebUtil_24110053.intParam(request, "id", -1);
        Book_24110053 book = service.book(id).orElse(null);
        if (book == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        List<Rating_24110053> ratings = service.ratings(id);
        double average = ratings.stream().mapToInt(item -> item.getRating()).average().orElse(0);
        request.setAttribute("book", book);
        request.setAttribute("ratings", ratings);
        request.setAttribute("averageRating", average);
        request.getRequestDispatcher("/WEB-INF/views/book-detail.jsp").forward(request, response);
    }
}
