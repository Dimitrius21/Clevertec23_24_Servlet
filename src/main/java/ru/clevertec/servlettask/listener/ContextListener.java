package ru.clevertec.servlettask.listener;

import ru.clevertec.servlettask.repository.RoleRepository;
import ru.clevertec.servlettask.repository.UserRepository;
import ru.clevertec.servlettask.service.RoleService;
import ru.clevertec.servlettask.service.UserService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;

@WebListener
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        RoleRepository roleRepository =  new RoleRepository();
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository, roleRepository);
        RoleService roleService = new RoleService(roleRepository);
        context.setAttribute("userService", userService);
        context.setAttribute("roleService", userService);
        context.setAttribute("sessions", new ConcurrentHashMap<Long, HttpSession>());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
