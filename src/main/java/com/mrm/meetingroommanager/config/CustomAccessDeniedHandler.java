package com.mrm.meetingroommanager.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // Add a flash message to the session
        request.getSession().setAttribute("accessDeniedMessage",
                "You don't have permission to access this feature. Only administrators can manage rooms.");

        // Redirect to the rooms list page
        response.sendRedirect(request.getContextPath() + "/rooms?accessDenied=true");
    }
}
