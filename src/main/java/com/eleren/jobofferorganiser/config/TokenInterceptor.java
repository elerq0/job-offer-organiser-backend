package com.eleren.jobofferorganiser.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return true;
        }

        String newToken = JwtService.updateExpirationDateToken(token);
        response.setHeader("Authorization", newToken);
        response.setHeader("Access-control-expose-headers", "Authorization");

        return true;
    }
}