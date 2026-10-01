package vn.iotstar.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public final class WebUtil_24110053 {
    private WebUtil_24110053() {}
    public static int intParam(HttpServletRequest request, String name, int fallback) {
        try { return Integer.parseInt(request.getParameter(name)); }
        catch (Exception ignored) { return fallback; }
    }
    public static String text(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }
    public static void flash(HttpSession session, String type, String message) {
        session.setAttribute("flashType", type);
        session.setAttribute("flashMessage", message);
    }
}
