package com.zentaskapi.config;

import com.zentaskapi.auth.filter.JwtAuthFilter;
import com.zentaskapi.auth.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
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
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(authenticationEntryPoint())
                )
                .authorizeHttpRequests(auth -> auth

                        // 🌐 Rutas públicas
                        .requestMatchers("/", "/ZenTaskAPI/").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()

                        // 👤 Usuarios
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN") // Listar todos
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").authenticated() // ADMIN, MANAGER, o el mismo
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN") // Crear
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").authenticated() // ADMIN o el mismo
                        .requestMatchers(HttpMethod.PATCH, "/api/users/{id}").authenticated() // ADMIN o el mismo
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasRole("ADMIN") // Eliminar
                        .requestMatchers(HttpMethod.GET, "/api/users/*/time-entries").hasAnyRole("ADMIN", "MANAGER", "USER")

                        // ✅ Tareas
                        .requestMatchers(HttpMethod.GET, "/api/tasks").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .requestMatchers(HttpMethod.GET, "/api/tasks/{id}").authenticated() // Participantes
                        .requestMatchers(HttpMethod.POST, "/api/tasks").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/{id}").authenticated() // Asignado o MANAGER
                        .requestMatchers(HttpMethod.PATCH, "/api/tasks/{id}/status").authenticated() // Asignado
                        .requestMatchers(HttpMethod.PATCH, "/api/tasks/{id}/assign").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/{id}").hasAnyRole("ADMIN", "MANAGER")

                        // ⏱️ Registros de tiempo
                        .requestMatchers(HttpMethod.POST, "/api/tasks/{id}/time-entries").authenticated() // Asignado
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}/time-entries").authenticated() // Admin, self, Manager

                        // 💬 Comentarios
                        .requestMatchers(HttpMethod.POST, "/api/tasks/{id}/comments").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tasks/{id}/comments").authenticated()

                        // 🏷️ Tags
                        .requestMatchers(HttpMethod.POST, "/api/tags").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/tags").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/tasks/{id}/tags").hasRole("MANAGER")

                        // 📎 Archivos adjuntos
                        .requestMatchers(HttpMethod.POST, "/api/tasks/{id}/attachments").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/tasks/{id}/attachments").authenticated()

                        // Todo lo demás requiere autenticación
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

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            String body = new ObjectMapper().writeValueAsString(Map.of("error", "No autorizado o token inválido"));
            response.getWriter().write(body);
        };
    }
}
