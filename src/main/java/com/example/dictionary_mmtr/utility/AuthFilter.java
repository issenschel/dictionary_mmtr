package com.example.dictionary_mmtr.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class AuthFilter extends OncePerRequestFilter {

    @Value("${admin.api.token}")
    private String adminApiToken;

    private final List<String> protectedMethods = Arrays.asList("DELETE", "POST", "PUT");
    private final List<String> protectedUris = Arrays.asList("/dictionary");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (requiresAuthentication(request)) {
            String token = request.getHeader("Authorization");

            if (token == null || !token.equals(adminApiToken)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("admin", null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        return protectedMethods.contains(request.getMethod().toUpperCase()) &&
               protectedUris.contains(request.getRequestURI());
    }
}
