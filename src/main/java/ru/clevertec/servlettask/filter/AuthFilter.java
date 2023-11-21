package ru.clevertec.servlettask.filter;

import org.apache.log4j.Logger;
import ru.clevertec.servlettask.entity.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;
import java.util.Set;

@WebFilter(value = {"/user"}, filterName = "2")

public class AuthFilter implements Filter {
    private static Logger logger = Logger.getLogger(AuthFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("AuthFilter");
        HttpServletRequest req = (HttpServletRequest) request;
        String method = req.getMethod().toLowerCase();
        if (!method.equals("get") && !method.equals("post")) {
            HttpSession session = req.getSession();
            Set<Role> roles = (Set<Role>) session.getAttribute("roles");
            if (Objects.nonNull(roles)) {
                if (method.equals("put")) {
                    if (roles.stream().noneMatch(arr -> arr.getRole().equals("ADMIN"))) {
                        sendResponse((HttpServletResponse) response, "Not authorized", 403);
                        return;
                    }
                }
                chain.doFilter(request, response);
            } else {
                sendResponse((HttpServletResponse) response, "Not authenticated", 401);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private void sendResponse(HttpServletResponse resp, String answer, int code) throws IOException {
        resp.getWriter().write(answer);
        resp.setContentType("text/plain");
        resp.setStatus(code);
    }

    @Override
    public void destroy() {
    }
}
