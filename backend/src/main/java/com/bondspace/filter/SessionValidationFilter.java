package com.bondspace.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SessionValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Exclude public endpoints from session validation
        String path = httpRequest.getRequestURI();
        if (path.equals("/api/auth/login") ||
                path.equals("/api/auth/register") ||
                path.equals("/api/auth/logout") ||
                path.startsWith("/public/") ||
                path.equals("/api/health")) {
            chain.doFilter(request, response);
            return;
        }

        // Check session attributes
        if (httpRequest.getSession().getAttribute("userId") == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Continue with the filter chain
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
