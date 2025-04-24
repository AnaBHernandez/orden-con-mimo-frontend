package com.ordenconmimo.orden_con_mimo_frontend.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@Component
public class SesionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        
        if (path.startsWith("/login") || path.startsWith("/css") || path.startsWith("/js") || path.startsWith("/images")) {
            return true;
        }
        
        HttpSession session = request.getSession();
        if (session.getAttribute("usuarioLogueado") == null) {
            response.sendRedirect("/login");
            return false;
        }
        
        return true;
    }
}

