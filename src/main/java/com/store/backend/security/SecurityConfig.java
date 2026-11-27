package com.store.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    public SecurityConfig(JwtService jwtService, TokenBlacklistService tokenBlacklistService) {
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                        .requestMatchers("/api/auth/me", "/api/auth/logout").authenticated()
                        // Các API nghiệp vụ khác giữ nguyên hành vi hiện tại (chưa yêu cầu đăng nhập)
                        // để không phá vỡ frontend hiện có; có thể siết lại sau khi frontend tích hợp JWT.
                        .anyRequest().permitAll()
                )
                // Khởi tạo trực tiếp (không phải bean @Component) để tránh Spring Boot
                // đăng ký thêm filter này như một servlet filter độc lập ngoài chuỗi này.
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, tokenBlacklistService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
