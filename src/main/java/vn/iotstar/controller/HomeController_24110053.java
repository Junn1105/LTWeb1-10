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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/home", "/products"})
public class HomeController_24110053 extends HttpServlet {
    private static final int PAGE_SIZE = 6;
    private final LibraryService_24110053 service = new LibraryServiceImpl_24110053();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = Math.max(1, WebUtil_24110053.intParam(request, "page", 1));
        int totalPages = service.bookPages(PAGE_SIZE);
        page = Math.min(page, totalPages);
        List<Book_24110053> books = service.books(page, PAGE_SIZE);
        Map<Integer, Integer> reviewCounts = new LinkedHashMap<>();
        for (Book_24110053 book : books) {
            reviewCounts.put(book.getBookid(), service.ratings(book.getBookid()).size());
        }
        request.setAttribute("books", books);
        request.setAttribute("reviewCounts", reviewCounts);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
