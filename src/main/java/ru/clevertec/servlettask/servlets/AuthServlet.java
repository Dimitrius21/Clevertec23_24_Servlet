package ru.clevertec.servlettask.servlets;

import ru.clevertec.servlettask.entity.User;
import ru.clevertec.servlettask.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@WebServlet({"/auth/signin", "/auth/signout"})
public class AuthServlet extends HttpServlet {
    UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        userService = (UserService) context.getAttribute("userService");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if (path.equals("/auth/signin")) {
            String authorizationHeader = request.getHeader("Authorization");
            String[] authorization = authorizationHeader.split(" ");
            String respMessage;
            int respCode = 400;
            if (Objects.nonNull(authorization) && authorization[0].equals("Basic")) {
                Base64.Decoder decoder = Base64.getDecoder();
                String credentials = new String(decoder.decode(authorization[1]));
                String[] data = credentials.split(":");
                User user = userService.getUserByUsername(data[0]).orElse(null);

                if (Objects.nonNull(user) && user.getPassword().equals(data[1])) {
                    HttpSession session = request.getSession();
                    session.setAttribute("roles", user.getRoles());
                    session.setAttribute("id", user.getId());
                    ServletContext context = request.getServletContext();
                    ((Map<Long, HttpSession>) context.getAttribute("sessions")).put(user.getId(), session);
                    respMessage = "Authenticated";
                    respCode = 200;
                } else {
                    respMessage = "Bad credentials";
                    respCode = 401;
                }
            } else {
                respMessage = "Bad authentication data";
            }
            response.getWriter().println(respMessage);
            response.setStatus(respCode);
        }else {
            HttpSession session = request.getSession();
            Long id = (Long) session.getAttribute("id");
            if (Objects.nonNull(id)){
                ((Map<Long, HttpSession>)request.getServletContext().getAttribute("sessions")).remove(id);
            }
            session.invalidate();
        }
    }
}
