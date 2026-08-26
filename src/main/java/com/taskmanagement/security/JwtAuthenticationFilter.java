package com.taskmanagement.security;

import com.taskmanagement.entity.User;
import com.taskmanagement.repository.UserRepository;
import com.taskmanagement.service.LogoutService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserRepository userRepository;

    private final LogoutService logoutService;


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        System.out.println(
                "AUTHORIZATION HEADER = "
                        + authHeader
        );


        // No Authorization header
        if (authHeader == null
                || !authHeader.startsWith("Bearer ")) {

            System.out.println(
                    "NO JWT TOKEN FOUND"
            );

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }


        // Extract JWT token
        String token =
                authHeader.substring(7);


        // Check whether token has been revoked
        if (logoutService.isTokenRevoked(token)) {

            System.out.println(
                    "JWT TOKEN HAS BEEN REVOKED"
            );

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType(
                    "application/json"
            );

            response.getWriter().write(
                    "{\"error\":\"Token has been revoked. Please login again.\"}"
            );

            return;
        }


        try {

            // Extract email from JWT
            String email =
                    jwtService.extractEmail(token);

            System.out.println(
                    "JWT EMAIL = ["
                            + email
                            + "]"
            );


            // Authenticate only when there is
            // no existing authentication
            if (email != null
                    && SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {


                // Find user using UserRepository
                User user =
                        userRepository
                                .findByEmail(email)
                                .orElse(null);


                // User does not exist
                if (user == null) {

                    System.out.println(
                            "USER NOT FOUND = ["
                                    + email
                                    + "]"
                    );

                    filterChain.doFilter(
                            request,
                            response
                    );

                    return;
                }


                // Validate JWT
                if (jwtService.isTokenValid(
                        token,
                        email)) {


                    // Convert user's role into
                    // Spring Security authority
                    SimpleGrantedAuthority authority =
                            new SimpleGrantedAuthority(
                                    "ROLE_"
                                            + user.getRole().name()
                            );


                    // Create authenticated user
                    UsernamePasswordAuthenticationToken
                            authentication =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    List.of(authority)
                            );


                    // Store authentication in SecurityContext
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authentication
                            );


                    System.out.println(
                            "JWT AUTHENTICATION SUCCESSFUL FOR = "
                                    + email
                    );
                }

            }

        } catch (Exception exception) {

            System.out.println(
                    "JWT VALIDATION FAILED = "
                            + exception.getMessage()
            );
        }


        // Continue the request
        filterChain.doFilter(
                request,
                response
        );
    }
}