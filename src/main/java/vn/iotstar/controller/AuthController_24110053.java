package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User_24110053;
import vn.iotstar.services.AuthServiceImpl_24110053;
import vn.iotstar.services.AuthService_24110053;
import vn.iotstar.util.MailUtil_24110053;
import vn.iotstar.util.WebUtil_24110053;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.Instant;

@WebServlet({"/login", "/register", "/verify-otp", "/logout"})
public class AuthController_24110053 extends HttpServlet {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final AuthService_24110053 authService = new AuthServiceImpl_24110053();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/logout".equals(path)) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        String view = switch (path) {
            case "/register" -> "/WEB-INF/views/auth/register.jsp";
            case "/verify-otp" -> "/WEB-INF/views/auth/verify-otp.jsp";
            default -> "/WEB-INF/views/auth/login.jsp";
        };
        request.getRequestDispatcher(view).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/login" -> login(request, response);
            case "/register" -> register(request, response);
            case "/verify-otp" -> verifyOtp(request, response);
            default -> response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String email = WebUtil_24110053.text(request, "email");
        String password = WebUtil_24110053.text(request, "password");
        User_24110053 user = authService.authenticate(email, password).orElse(null);
        if (user == null) {
            request.setAttribute("error", "Email hoặc mật khẩu không đúng.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
            return;
        }
        request.getSession().setAttribute("currentUser", user);
        response.sendRedirect(request.getContextPath()
                + (Boolean.TRUE.equals(user.getAdmin()) ? "/admin/books" : "/home"));
    }

    private void register(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String email = WebUtil_24110053.text(request, "email");
        String fullname = WebUtil_24110053.text(request, "fullname");
        String password = WebUtil_24110053.text(request, "password");
        String confirm = WebUtil_24110053.text(request, "confirmPassword");
        int phone = WebUtil_24110053.intParam(request, "phone", 0);
        if (email.isBlank() || fullname.isBlank() || password.length() < 6 || !password.equals(confirm)) {
            request.setAttribute("error",
                    "Vui lòng nhập đủ thông tin; mật khẩu tối thiểu 6 ký tự và phải trùng nhau.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }
        if (authService.emailExists(email)) {
            request.setAttribute("error", "Email này đã được sử dụng.");
            request.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(request, response);
            return;
        }

        User_24110053 pending = new User_24110053();
        pending.setEmail(email);
        pending.setFullname(fullname);
        pending.setPhone(phone == 0 ? null : phone);
        pending.setPasswd(password);
        String otp = String.format("%06d", RANDOM.nextInt(1_000_000));
        HttpSession session = request.getSession();
        session.setAttribute("pendingUser", pending);
        session.setAttribute("registrationOtp", otp);
        session.setAttribute("otpExpiresAt", Instant.now().plusSeconds(300).toEpochMilli());
        boolean mailed = MailUtil_24110053.sendOtp(email, otp);
        if (!mailed) session.setAttribute("developmentOtp", otp);
        response.sendRedirect(request.getContextPath() + "/verify-otp");
    }

    private void verifyOtp(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        User_24110053 pending = (User_24110053) session.getAttribute("pendingUser");
        String expected = (String) session.getAttribute("registrationOtp");
        Long expiresAt = (Long) session.getAttribute("otpExpiresAt");
        String entered = WebUtil_24110053.text(request, "otp");
        if (pending == null || expected == null || expiresAt == null
                || Instant.now().toEpochMilli() > expiresAt) {
            request.setAttribute("error", "Phiên đăng ký hoặc mã OTP đã hết hạn. Vui lòng đăng ký lại.");
            request.getRequestDispatcher("/WEB-INF/views/auth/verify-otp.jsp").forward(request, response);
            return;
        }
        if (!expected.equals(entered)) {
            request.setAttribute("error", "Mã OTP không chính xác.");
            request.getRequestDispatcher("/WEB-INF/views/auth/verify-otp.jsp").forward(request, response);
            return;
        }
        authService.register(pending);
        session.removeAttribute("pendingUser");
        session.removeAttribute("registrationOtp");
        session.removeAttribute("otpExpiresAt");
        session.removeAttribute("developmentOtp");
        WebUtil_24110053.flash(session, "success", "Đăng ký thành công. Bạn có thể đăng nhập.");
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
