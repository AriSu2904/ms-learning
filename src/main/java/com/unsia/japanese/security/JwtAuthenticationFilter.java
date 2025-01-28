package com.unsia.japanese.security;

import com.unsia.japanese.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@WebFilter(urlPatterns = "/api/v1/jps/*")
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Value("${unsia.issuer}")
    private String authIssuer;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = extractToken(request.getHeader("Authorization"));

        if (token != null && jwtService.validateToken(token)) {
            Claims claims = jwtService.extractAllClaims(token);

            String issuer = claims.getIssuer();
            if (authIssuer.equals(issuer)) {
                String studentId = claims.getSubject();

                Object rolesClaim = claims.get("role");
                if (!(rolesClaim instanceof List)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid role claim");
                    return;
                }

                List<SimpleGrantedAuthority> authorities = generateAuthority((List<Map<String, String>>) rolesClaim, studentId);

                log.info("Authenticated user with ID: {} and roles: {}", studentId, authorities);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid issuer");
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or missing token");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static List<SimpleGrantedAuthority> generateAuthority(List<Map<String, String>> rolesClaim, String studentId) {

        List<SimpleGrantedAuthority> authorities = rolesClaim.stream()
                .map(roleMap -> {
                    String role = roleMap.get("authority");
                    if (role != null) {
                        return new SimpleGrantedAuthority(role);
                    } else {
                        throw new RuntimeException("Invalid role in JWT");
                    }
                })
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(studentId, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authorities;
    }

    private String extractToken(String authHeader) {
        return authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;
    }
}
