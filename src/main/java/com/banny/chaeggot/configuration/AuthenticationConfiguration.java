package com.banny.chaeggot.configuration;

import com.banny.chaeggot.configuration.filter.JwtTokenFilter;
import com.banny.chaeggot.exception.CustomAuthenticationEntryPoint;
import com.banny.chaeggot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfiguration {

    private final UserService userService;

    @Value("${jwt.secret-key}")
    private String key;

    /**
     * The below urls are ignored by the filter.
     * The requestMatchers() is used to ignore the specified urls.
     * @return WebSecurityCustomizer
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/api/*/user/join", "/api/*/user/login");
    }

    /**
     * SecurityFilterChain is an interface that defines the filter chain for Spring Security.
     * TODO:
     * UsernamePasswordAuthenticationFilter
     * exceptionHandling()
     * authenticationEntryPoint()
     * CustomAuthenticationEntryPoint
     *
     * @param http
     * @return SecurityFilterChain
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // CSRF protection is disabled
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests.requestMatchers("/api/**").authenticated()) // All requests under /api/** are authenticated
                .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Session management is set to STATELESS. No session is created.
                .addFilterBefore(new JwtTokenFilter(key, userService), UsernamePasswordAuthenticationFilter.class) // JwtTokenFilter is added before UsernamePasswordAuthenticationFilter
                .exceptionHandling(authenticationManager -> authenticationManager.authenticationEntryPoint(new CustomAuthenticationEntryPoint())) // CustomAuthenticationEntryPoint is used for error handling when authentication fails
                .build();
    }
}
