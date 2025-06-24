package com.example.zentaskapi.config;

import com.example.zentaskapi.auth.filter.JwtAuthFilter;
import com.example.zentaskapi.auth.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                .authorizeHttpRequests(auth -> auth

                        // Rutas públicas
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
}
