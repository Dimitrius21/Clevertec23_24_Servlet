package ru.clevertec.servlettask.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import ru.clevertec.servlettask.domain.ExceptionDto;
import ru.clevertec.servlettask.entity.Role;
import ru.clevertec.servlettask.exception.IncorrectRequestDataException;
import ru.clevertec.servlettask.service.RoleService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/role")
public class RoleServlet extends HttpServlet {
    private RoleService roleService;
    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        roleService = (RoleService) config.getServletContext().getAttribute("roleService");
        gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParameter = req.getParameter("id");
        try {
            Long id = Long.parseLong(idParameter);
            Role role = roleService.getRole(id);
            sendResp(resp, role, 200);
        } catch (NumberFormatException | IncorrectRequestDataException e) {
            ExceptionDto dto = new ExceptionDto("Role has not found", idParameter);
            sendResp(resp, dto, 400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Role role = getFromRequest(req);
            Role role1 = roleService.createRole(role);
            sendResp(resp, role1, 201);
        } catch (JsonSyntaxException e) {
            ExceptionDto dto = new ExceptionDto(e.getMessage(), "400");
            sendResp(resp, dto, 400);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Role role = getFromRequest(req);
            Role role1 = roleService.updateRole(role);
            sendResp(resp, role1, 201);
        } catch (IncorrectRequestDataException | JsonSyntaxException e) {
            ExceptionDto dto = new ExceptionDto(e.getMessage(), "400");
            sendResp(resp, dto, 400);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));
            roleService.deleteRole(id);
            sendResp(resp, null, 200);
        }catch (NumberFormatException e){
            ExceptionDto dto = new ExceptionDto("Incorrect id", req.getParameter("id"));
            sendResp(resp, dto, 400);
        }
    }

    private void sendResp(HttpServletResponse response, Object o, int code) throws IOException {
        String user1 = gson.toJson(o);
        response.getWriter().write(user1);
        response.setStatus(code);
        response.setContentType("application/json");
    }

    private Role getFromRequest(HttpServletRequest request) throws IOException {
        String res = (String) request.getAttribute("body");
        return gson.fromJson(res, Role.class);
    }
}
