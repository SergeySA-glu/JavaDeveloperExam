package com.tasktracker.security.filter;

import com.tasktracker.model.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_NAME = "Authorization";

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    public AuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    private boolean isHeaderValid(String authHeader) {
        return !(authHeader == null || !authHeader.startsWith(BEARER_PREFIX));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_NAME);

        if (!isHeaderValid(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(BEARER_PREFIX.length());
        try {
            String email = jwtService.extractUsername(jwt);
            if (email == null || email.isEmpty() || SecurityContextHolder.getContext().getAuthentication() != null) {
                filterChain.doFilter(request, response);
                return;
            }

            UserDetails details = userDetailsService.loadUserByUsername(email);

            if (jwtService.isTokenValid(jwt, details)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        details, null, details.getAuthorities()
                );

                token.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                context.setAuthentication(token);
                SecurityContextHolder.setContext(context);
            }

        } catch (ExpiredJwtException | MalformedJwtException ignored) {
        }

        filterChain.doFilter(request, response);
    }
}
