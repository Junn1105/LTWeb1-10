package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.iotstar.entity.*;
import vn.iotstar.services.*;
import vn.iotstar.util.WebUtil_24110053;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@WebServlet({"/cart", "/checkout", "/orders"})
public class ShoppingController_24110053 extends HttpServlet {
    private final ShoppingService_24110053 service = new ShoppingService_24110053();
    private String key(User_24110053 user) { return "cart_" + user.getId(); }
    private Cart_24110053 cart(HttpSession session, User_24110053 user) {
        String key = key(user);
        Cart_24110053 cart = (Cart_24110053) session.getAttribute(key);
        if (cart == null) { cart = new Cart_24110053(); session.setAttribute(key, cart); }
        return cart;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Cache-Control", "no-store");
        HttpSession session = request.getSession();
        User_24110053 user = (User_24110053) session.getAttribute("currentUser");
        String path = request.getServletPath();
        if ("/orders".equals(path)) {
            String code = WebUtil_24110053.text(request, "status");
            OrderStatus_24110053 status = null;
            try { if (!code.isEmpty()) status = OrderStatus_24110053.valueOf(code); }
            catch (IllegalArgumentException ex) { response.sendError(400, "Trạng thái không hợp lệ."); return; }
            request.setAttribute("statuses", OrderStatus_24110053.values());
            request.setAttribute("selectedStatus", code);
            request.setAttribute("orders", service.orders(user.getId(), status));
        } else {
            synchronized (session) {
                List<ShoppingService_24110053.Line> lines = service.lines(cart(session, user).snapshot());
                request.setAttribute("lines", lines);
                request.setAttribute("total", lines.stream().map(ShoppingService_24110053.Line::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add));
                boolean ready = !lines.isEmpty() && lines.stream().allMatch(ShoppingService_24110053.Line::isAvailable);
                request.setAttribute("ready", ready);
                if ("/checkout".equals(path)) {
                    String token = UUID.randomUUID().toString();
                    session.setAttribute(key(user) + "_checkout", token);
                    request.setAttribute("checkoutToken", token);
                    Map<Integer, BigDecimal> prices = new HashMap<>();
                    lines.forEach(line -> prices.put(line.getBookId(), line.getPrice()));
                    session.setAttribute(key(user) + "_prices", prices);
                }
            }
        }
        request.getRequestDispatcher("/WEB-INF/views/shop" + path + ".jsp").forward(request, response);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (!Objects.equals(session.getAttribute("csrfToken"), request.getParameter("csrfToken"))
                || session.getAttribute("csrfToken") == null) { response.sendError(403); return; }
        User_24110053 user = (User_24110053) session.getAttribute("currentUser");
        if ("/orders".equals(request.getServletPath())) { response.sendError(405); return; }
        String redirect = "/cart";
        synchronized (session) {
            Cart_24110053 cart = cart(session, user);
            try {
                if ("/cart".equals(request.getServletPath())) {
                    String action = WebUtil_24110053.text(request, "action");
                    int id = WebUtil_24110053.intParam(request, "bookId", -1);
                    int quantity = WebUtil_24110053.intParam(request, "quantity", -1);
                    switch (action) {
                        case "add" -> cart.add(id, quantity, service.stock(id));
                        case "update" -> cart.update(id, quantity, service.stock(id));
                        case "remove" -> cart.remove(id);
                        case "clear" -> cart.clear();
                        default -> throw new IllegalArgumentException("Thao tác giỏ hàng không hợp lệ.");
                    }
                    session.removeAttribute(key(user) + "_checkout");
                    WebUtil_24110053.flash(session, "success", "Đã cập nhật giỏ hàng.");
                } else {
                    Object token = session.getAttribute(key(user) + "_checkout");
                    if (token == null || !token.equals(request.getParameter("checkoutToken")))
                        throw new IllegalArgumentException("Phiên thanh toán đã thay đổi hoặc đã được xử lý. Vui lòng kiểm tra lịch sử đơn hàng hoặc mở lại thanh toán.");
                    int orderId = service.checkout(user.getId(), cart.snapshot(),
                            (Map<Integer, BigDecimal>) session.getAttribute(key(user) + "_prices"),
                            WebUtil_24110053.text(request, "recipient"), WebUtil_24110053.text(request, "phone"),
                            WebUtil_24110053.text(request, "address"), WebUtil_24110053.text(request, "note"));
                    cart.clear();
                    session.removeAttribute(key(user) + "_checkout");
                    session.removeAttribute(key(user) + "_prices");
                    WebUtil_24110053.flash(session, "success", "Đặt hàng #" + orderId + " thành công. Thanh toán tiền mặt khi nhận hàng (COD).");
                    redirect = "/orders";
                }
            } catch (IllegalArgumentException ex) {
                if ("/checkout".equals(request.getServletPath())) {
                    request.setAttribute("error", ex.getMessage());
                    request.setAttribute("submitted", true);
                    doGet(request, response);
                    return;
                }
                WebUtil_24110053.flash(session, "danger", ex.getMessage());
            } catch (RuntimeException ex) {
                log("Shopping operation failed", ex);
                WebUtil_24110053.flash(session, "danger", "Không thể xử lý lúc này. Giỏ hàng được giữ lại, vui lòng thử lại.");
            }
        }
        response.sendRedirect(request.getContextPath() + redirect);
    }
}
