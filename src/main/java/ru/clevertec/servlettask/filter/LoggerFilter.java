package ru.clevertec.servlettask.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@WebFilter(value = "/*", filterName = "1")
public class LoggerFilter implements Filter {

    private static Logger logger = Logger.getLogger(LoggerFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("LoggerFilter");
        HttpServletRequest req = (HttpServletRequest) request;
        String headers = Collections.list(req.getHeaderNames()).stream()
                .map(it -> it + ": " + req.getHeader(it))
                .collect(Collectors.joining("\n"));
        logger.info(headers);
        String method = req.getMethod().toUpperCase();
        String requestURL = req.getRequestURL().toString();
        String queryString = req.getQueryString();
        queryString = Objects.nonNull(queryString) ? "?" + queryString : "";

        logger.info(String.format("%s request by %s%s", method, requestURL, queryString));
        String requestBody = req.getReader().lines().collect(Collectors.joining());
        logger.info("Request body: " + requestBody);
        req.setAttribute("body", requestBody);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
