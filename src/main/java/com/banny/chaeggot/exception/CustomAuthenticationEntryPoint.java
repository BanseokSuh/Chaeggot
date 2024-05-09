package com.banny.chaeggot.exception;

import com.banny.chaeggot.controller.response.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * If authentication fails, this class is called.
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * TODO:
     *
     * @param request
     * @param response
     * @param authException This exception contains the reason for the authentication failure.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(ErrorCode.INVALID_TOKEN.getHttpStatus().value());
        response.getWriter().write(Response.error(ErrorCode.INVALID_TOKEN.getHttpStatus(), ErrorCode.INVALID_TOKEN.getCode(), ErrorCode.INVALID_TOKEN.getMessage()).toStream());
    }
}
