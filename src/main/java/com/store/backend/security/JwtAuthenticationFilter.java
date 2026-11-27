package com.store.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Không đánh dấu @Component: filter này chỉ được đăng ký thủ công bên trong
 * SecurityConfig (addFilterBefore). Nếu để Spring Boot tự phát hiện như một
 * bean Filter thông thường, nó sẽ bị đăng ký thêm một lần nữa như một servlet
 * filter độc lập nằm ngoài chuỗi Spring Security, chạy trước
 * SecurityContextHolderFilter rồi bị ghi đè mất, trong khi OncePerRequestFilter
 * lại chặn nó chạy lần hai đúng vị trí trong chuỗi Spring Security.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String taiKhoan = jwtService.extractTaiKhoan(token);
                boolean chuaXacThuc = SecurityContextHolder.getContext().getAuthentication() == null;

                if (taiKhoan != null && chuaXacThuc && jwtService.isTokenValid(token, taiKhoan)) {
                    String chucVu = jwtService.extractChucVu(token);
                    List<SimpleGrantedAuthority> authorities =
                            List.of(new SimpleGrantedAuthority("ROLE_" + chucVu));

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(taiKhoan, null, authorities);
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                }
            } catch (Exception ignored) {
                // Token không hợp lệ hoặc hết hạn: bỏ qua, request tiếp tục như chưa xác thực
            }
        }

        filterChain.doFilter(request, response);
    }
}
