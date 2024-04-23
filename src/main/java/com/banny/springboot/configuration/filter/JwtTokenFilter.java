package com.banny.springboot.configuration.filter;

import com.banny.springboot.model.User;
import com.banny.springboot.service.UserService;
import com.banny.springboot.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String secretKey;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;

        /**
         * the Authorization header is not present, return.
         */
        try {
            if (header == null || !header.startsWith("Bearer ")) {
                log.error("Authorization Header does not start with Bearer {}", request.getRequestURI());

                filterChain.doFilter(request, response);
                return;
            } else {
                token = header.split(" ")[1].trim();
            }

            /**
             * Get userId from the token
             */
            String userId = JwtTokenUtils.getUserId(token, secretKey);

            /**
             * Load user info by userId
             */
            User userDetails = userService.loadUserByUserId(userId);

            /**
             * Check if the token is valid
             */
            if (!JwtTokenUtils.isTokenValid(token, userId, secretKey)) {
                filterChain.doFilter(request, response);
                return;
            }

            /**
             * principal: The user object that is authenticated
             * credentials: The password of the user
             * authorities: The roles of the user
             */
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RuntimeException e) {
            log.error("Error occurs while validating. {}", e.toString());
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
