package vn.iotstar.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebFilter("/*")
public class ShoppingRequestFilter_24110053 implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        HttpServletRequest request = (HttpServletRequest) req;
        if (!request.getServletPath().startsWith("/assets/")) {
            HttpSession session = request.getSession();
            synchronized (session) {
                if (session.getAttribute("csrfToken") == null)
                    session.setAttribute("csrfToken", UUID.randomUUID().toString());
            }
        }
        chain.doFilter(req, res);
    }
}
