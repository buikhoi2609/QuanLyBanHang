package com.store.backend.service;

import com.store.backend.dto.LoginRequest;
import com.store.backend.dto.RegisterRequest;
import com.store.backend.model.NhanVien;
import com.store.backend.repository.NhanVienRepository;
import com.store.backend.security.JwtService;
import com.store.backend.security.TokenBlacklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private NhanVienRepository nhanVienRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setHoTen("Nguyen Van A");
        registerRequest.setTaiKhoan("vana");
        registerRequest.setMatKhau("matkhau123");

        loginRequest = new LoginRequest();
        loginRequest.setTaiKhoan("vana");
        loginRequest.setMatKhau("matkhau123");
    }

    // ---------- register ----------

    @Test
    void register_thieuTaiKhoan_traVeThatBai() {
        registerRequest.setTaiKhoan(" ");

        Map<String, Object> result = authService.register(registerRequest);

        assertThat(result.get("success")).isEqualTo(false);
        verify(nhanVienRepository, never()).save(any());
    }

    @Test
    void register_thieuMatKhau_traVeThatBai() {
        registerRequest.setMatKhau(null);

        Map<String, Object> result = authService.register(registerRequest);

        assertThat(result.get("success")).isEqualTo(false);
        verify(nhanVienRepository, never()).save(any());
    }

    @Test
    void register_taiKhoanDaTonTai_traVeThatBai() {
        when(nhanVienRepository.existsByTaiKhoan("vana")).thenReturn(true);

        Map<String, Object> result = authService.register(registerRequest);

        assertThat(result.get("success")).isEqualTo(false);
        assertThat(result.get("message")).isEqualTo("Tên tài khoản đã tồn tại");
        verify(nhanVienRepository, never()).save(any());
    }

    @Test
    void register_hopLe_maHoaMatKhauVaLuuThanhCong() {
        when(nhanVienRepository.existsByTaiKhoan("vana")).thenReturn(false);
        when(passwordEncoder.encode("matkhau123")).thenReturn("MA_HOA_BCRYPT");
        when(nhanVienRepository.save(any(NhanVien.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Map<String, Object> result = authService.register(registerRequest);

        assertThat(result.get("success")).isEqualTo(true);

        verify(nhanVienRepository).save(argThat(nv ->
                nv.getMatKhau().equals("MA_HOA_BCRYPT")
                        && nv.getTaiKhoan().equals("vana")
                        && Boolean.TRUE.equals(nv.getTrangThai())
                        && "NhanVien".equals(nv.getChucVu())
        ));
    }

    // ---------- login ----------

    @Test
    void login_taiKhoanKhongTonTai_traVeThatBai() {
        when(nhanVienRepository.findByTaiKhoan("vana")).thenReturn(Optional.empty());

        Map<String, Object> result = authService.login(loginRequest);

        assertThat(result.get("success")).isEqualTo(false);
        assertThat(result.get("message")).isEqualTo("Tài khoản không tồn tại");
    }

    @Test
    void login_taiKhoanBiKhoa_traVeThatBai() {
        NhanVien nv = taoNhanVien();
        nv.setTrangThai(false);
        when(nhanVienRepository.findByTaiKhoan("vana")).thenReturn(Optional.of(nv));

        Map<String, Object> result = authService.login(loginRequest);

        assertThat(result.get("success")).isEqualTo(false);
        assertThat(result.get("message")).isEqualTo("Tài khoản đã bị khóa");
    }

    @Test
    void login_saiMatKhau_traVeThatBai() {
        NhanVien nv = taoNhanVien();
        when(nhanVienRepository.findByTaiKhoan("vana")).thenReturn(Optional.of(nv));
        when(passwordEncoder.matches("matkhau123", nv.getMatKhau())).thenReturn(false);

        Map<String, Object> result = authService.login(loginRequest);

        assertThat(result.get("success")).isEqualTo(false);
        assertThat(result.get("message")).isEqualTo("Mật khẩu không chính xác");
        verify(jwtService, never()).generateToken(anyString(), anyString());
    }

    @Test
    void login_thanhCong_traVeTokenVaAnMatKhau() {
        NhanVien nv = taoNhanVien();
        when(nhanVienRepository.findByTaiKhoan("vana")).thenReturn(Optional.of(nv));
        when(passwordEncoder.matches("matkhau123", nv.getMatKhau())).thenReturn(true);
        when(jwtService.generateToken("vana", "NhanVien")).thenReturn("FAKE_JWT_TOKEN");

        Map<String, Object> result = authService.login(loginRequest);

        assertThat(result.get("success")).isEqualTo(true);
        assertThat(result.get("token")).isEqualTo("FAKE_JWT_TOKEN");

        NhanVien userInResponse = (NhanVien) result.get("user");
        assertThat(userInResponse.getMatKhau()).isNull();
    }

    // ---------- logout ----------

    @Test
    void logout_coToken_blacklistToken() {
        when(jwtService.extractExpiration("FAKE_JWT_TOKEN"))
                .thenReturn(new java.util.Date(System.currentTimeMillis() + 60000));

        Map<String, Object> result = authService.logout("FAKE_JWT_TOKEN");

        assertThat(result.get("success")).isEqualTo(true);
        verify(tokenBlacklistService).blacklist(eq("FAKE_JWT_TOKEN"), any());
    }

    @Test
    void logout_khongCoToken_vanTraVeThanhCong() {
        Map<String, Object> result = authService.logout(null);

        assertThat(result.get("success")).isEqualTo(true);
        verify(tokenBlacklistService, never()).blacklist(anyString(), any());
    }

    private NhanVien taoNhanVien() {
        NhanVien nv = new NhanVien();
        nv.setMaNV(1);
        nv.setHoTen("Nguyen Van A");
        nv.setTaiKhoan("vana");
        nv.setMatKhau("MA_HOA_BCRYPT");
        nv.setChucVu("NhanVien");
        nv.setTrangThai(true);
        return nv;
    }
}
