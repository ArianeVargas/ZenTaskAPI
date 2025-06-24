package com.example.zentaskapi.config;

import com.example.zentaskapi.auth.filter.JwtAuthFilter;
import com.example.zentaskapi.auth.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, CustomUserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler())
                )
                .authorizeHttpRequests(auth -> auth

                        // Rutas públicas
                        .requestMatchers("/", "/ZenTaskAPI/").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // Rutas de administración solo para ADMIN
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/tags/**").hasRole("ADMIN")

                        // Tareas: acceso a USER, MANAGER y ADMIN
                        .requestMatchers("/api/tasks/**").hasAnyRole("USER", "MANAGER", "ADMIN")

                        // Comentarios: acceso a usuarios autenticados
                        .requestMatchers("/api/tasks/**/comments").authenticated()

                        // Registros de tiempo
                        .requestMatchers("/api/users/*/time-entries").hasAnyRole("USER", "MANAGER", "ADMIN")
                        .requestMatchers("/api/tasks/*/time-entries").hasAnyRole("USER", "MANAGER", "ADMIN")

                        // Archivos adjuntos
                        .requestMatchers("/api/tasks/**/attachments").hasAnyRole("USER", "MANAGER", "ADMIN")

                        // Cualquier otra petición requiere autenticación
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            String body = new ObjectMapper().writeValueAsString(Map.of("error", "Acceso denegado"));
            response.getWriter().write(body);
        };
    }
}
