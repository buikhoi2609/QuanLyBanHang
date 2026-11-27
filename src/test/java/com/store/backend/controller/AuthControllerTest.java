package com.store.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.backend.dto.LoginRequest;
import com.store.backend.dto.RegisterRequest;
import com.store.backend.security.JwtService;
import com.store.backend.security.SecurityConfig;
import com.store.backend.security.TokenBlacklistService;
import com.store.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@Import({SecurityConfig.class, JwtService.class, TokenBlacklistService.class})
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Test
    void register_hopLe_traVe201() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setTaiKhoan("vana");
        request.setMatKhau("matkhau123");

        Map<String, Object> serviceResult = new HashMap<>();
        serviceResult.put("success", true);
        serviceResult.put("message", "Đăng ký thành công");

        when(authService.register(any(RegisterRequest.class))).thenReturn(serviceResult);

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void register_thatBai_traVe400() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setTaiKhoan("vana");
        request.setMatKhau("matkhau123");

        Map<String, Object> serviceResult = new HashMap<>();
        serviceResult.put("success", false);
        serviceResult.put("message", "Tên tài khoản đã tồn tại");

        when(authService.register(any(RegisterRequest.class))).thenReturn(serviceResult);

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void login_hopLe_traVeTokenVa200() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setTaiKhoan("vana");
        request.setMatKhau("matkhau123");

        Map<String, Object> serviceResult = new HashMap<>();
        serviceResult.put("success", true);
        serviceResult.put("token", "FAKE_JWT_TOKEN");

        when(authService.login(any(LoginRequest.class))).thenReturn(serviceResult);

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.token").value("FAKE_JWT_TOKEN"));
    }

    @Test
    void login_saiMatKhau_traVe401() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setTaiKhoan("vana");
        request.setMatKhau("saimatkhau");

        Map<String, Object> serviceResult = new HashMap<>();
        serviceResult.put("success", false);
        serviceResult.put("message", "Mật khẩu không chính xác");

        when(authService.login(any(LoginRequest.class))).thenReturn(serviceResult);

        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void logout_coTokenHopLe_traVe200() throws Exception {
        Map<String, Object> serviceResult = new HashMap<>();
        serviceResult.put("success", true);
        serviceResult.put("message", "Đăng xuất thành công");

        when(authService.logout(anyString())).thenReturn(serviceResult);

        String token = jwtService.generateToken("vana", "NhanVien");

        mockMvc.perform(post("/api/auth/logout")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(authService).logout(token);
    }

    @Test
    void logout_khongCoToken_traVe401Hoac403() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void me_khongCoToken_traVe401Hoac403() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void me_coTokenHopLe_traVeThongTinNguoiDung() throws Exception {
        Map<String, Object> serviceResult = new HashMap<>();
        serviceResult.put("success", true);

        when(authService.getProfile(eq("vana"))).thenReturn(serviceResult);

        String token = jwtService.generateToken("vana", "NhanVien");

        mockMvc.perform(get("/api/auth/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(authService).getProfile("vana");
    }

    @Test
    void me_tokenGia_traVe401Hoac403() throws Exception {
        mockMvc.perform(get("/api/auth/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer token-khong-hop-le"))
                .andExpect(status().is4xxClientError());
    }
}
