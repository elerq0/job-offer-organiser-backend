package com.eleren.jobofferorganiser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            SecurityContextHolder.getContext().setAuthentication(null);
            chain.doFilter(request, response);
            return;
        }

        try {
            JwtService.verifyToken(token);
        } catch (JwtException e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            response.setStatus(401);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getWriter(), e.getMessage());
            return;
        }

        Claims claims = JwtService.getClaimsFromToken(token);

        List<String> authorities = claims.get("authorities", ArrayList.class);

        List<SimpleGrantedAuthority> roleList = new ArrayList<>();
        if (authorities != null && !authorities.isEmpty()) {
            roleList = authorities.stream()
                    .map(role -> new SimpleGrantedAuthority(role))
                    .collect(Collectors.toList());
        }

        String username = claims.getSubject();
        if (username != null) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null, roleList));
            chain.doFilter(request, response);
        } else {
            SecurityContextHolder.getContext().setAuthentication(null);
            response.setStatus(401);
        }

    }
}