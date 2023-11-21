package ru.clevertec.servlettask.servlets;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.clevertec.servlettask.domain.ExceptionDto;
import ru.clevertec.servlettask.entity.Role;
import ru.clevertec.servlettask.entity.User;
import ru.clevertec.servlettask.exception.IncorrectRequestDataException;
import ru.clevertec.servlettask.service.UserService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private UserService userService;
    private Gson gson;

    @Override
    public void init(ServletConfig config) {
        userService = (UserService) config.getServletContext().getAttribute("userService");
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParameter = req.getParameter("id");
        try {
            Long id = Long.parseLong(idParameter);
            User user = userService.getUser(id);
            sendResp(resp, user, 200);
        } catch (NumberFormatException | IncorrectRequestDataException e) {
            ExceptionDto dto = new ExceptionDto("User has not found", idParameter);
            sendResp(resp, dto, 400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = getFromRequest(req);
            User user1 = userService.createUser(user);
            sendResp(resp, user1, 201);
        } catch (JsonSyntaxException | IncorrectRequestDataException e) {
            ExceptionDto dto = new ExceptionDto(e.getMessage(), "400");
            sendResp(resp, dto, 400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = getFromRequest(req);
            user = userService.updateUser(user);
            Set<Role> roles = user.getRoles();
            Map<Long, HttpSession> sessions = (Map<Long, HttpSession>) req.getServletContext().getAttribute("sessions");
            HttpSession session = sessions.get(user.getId());
            if (session != null) {
                session.setAttribute("roles", roles);
            }
            sendResp(resp, user, 201);
        } catch (IncorrectRequestDataException | JsonSyntaxException e) {
            ExceptionDto dto = new ExceptionDto(e.getMessage(), "400");
            sendResp(resp, dto, 400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParameter = req.getParameter("id");
        try {
            long id = Long.parseLong(idParameter);
            Set<Role> roles = (Set<Role>) req.getSession().getAttribute("roles");
            if (isAdmin(roles) || id == (Long) req.getSession().getAttribute("id")) {
                userService.deleteUser(id);
                Map<Long, HttpSession> sessions = (Map<Long, HttpSession>) req.getServletContext().getAttribute("sessions");
                HttpSession session = sessions.get(id);
                if (session != null) {
                    session.invalidate();
                    sessions.remove(id);
                }
                sendResp(resp, "Has been deleted", 200);
            }else {
                sendResp(resp, "Not authorised", 200);
            }
        } catch (NumberFormatException e) {
            ExceptionDto dto = new ExceptionDto("User has not found", idParameter);
            sendResp(resp, dto, 400);
        }
    }

    private void sendResp(HttpServletResponse response, Object o, int code) throws IOException {
        String user1 = gson.toJson(o);
        response.getWriter().write(user1);
        response.setStatus(code);
        response.setContentType("application/json");
    }

    private User getFromRequest(HttpServletRequest request) throws IOException {
        String res = (String) request.getAttribute("body");
        return gson.fromJson(res, User.class);
    }

    private boolean isAdmin(Set<Role> roles) {
        return roles.stream().anyMatch(r -> r.getRole().equalsIgnoreCase("ADMIN"));
    }
}
