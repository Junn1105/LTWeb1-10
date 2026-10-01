package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.entity.User_24110053;
import vn.iotstar.services.LibraryServiceImpl_24110053;
import vn.iotstar.services.LibraryService_24110053;
import vn.iotstar.util.WebUtil_24110053;

import java.io.IOException;

@WebServlet("/review")
public class ReviewController_24110053 extends HttpServlet {
    private final LibraryService_24110053 service = new LibraryServiceImpl_24110053();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User_24110053 user = (User_24110053) request.getSession().getAttribute("currentUser");
        int bookId = WebUtil_24110053.intParam(request, "bookId", -1);
        try {
            service.rate(user.getId(), bookId,
                    WebUtil_24110053.intParam(request, "rating", 5),
                    WebUtil_24110053.text(request, "reviewText"));
            WebUtil_24110053.flash(request.getSession(), "success", "Đã lưu đánh giá của bạn.");
        } catch (RuntimeException exception) {
            WebUtil_24110053.flash(request.getSession(), "danger", exception.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/book?id=" + bookId);
    }
}
