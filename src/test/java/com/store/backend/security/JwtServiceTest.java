package com.store.backend.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET = "unit-test-secret-key-must-be-at-least-32-bytes-long";

    private JwtService newService(long expirationMs) {
        return new JwtService(SECRET, expirationMs);
    }

    @Test
    void generateToken_thenExtractTaiKhoan_traVeDungTaiKhoan() {
        JwtService jwtService = newService(60_000);

        String token = jwtService.generateToken("khoi123", "Admin");

        assertThat(jwtService.extractTaiKhoan(token)).isEqualTo("khoi123");
    }

    @Test
    void generateToken_thenExtractChucVu_traVeDungChucVu() {
        JwtService jwtService = newService(60_000);

        String token = jwtService.generateToken("khoi123", "Admin");

        assertThat(jwtService.extractChucVu(token)).isEqualTo("Admin");
    }

    @Test
    void isTokenValid_voiTaiKhoanDungVaChuaHetHan_traVeTrue() {
        JwtService jwtService = newService(60_000);

        String token = jwtService.generateToken("khoi123", "NhanVien");

        assertThat(jwtService.isTokenValid(token, "khoi123")).isTrue();
    }

    @Test
    void isTokenValid_voiTaiKhoanKhongKhop_traVeFalse() {
        JwtService jwtService = newService(60_000);

        String token = jwtService.generateToken("khoi123", "NhanVien");

        assertThat(jwtService.isTokenValid(token, "nguoiKhac")).isFalse();
    }

    @Test
    void isTokenValid_voiTokenDaHetHan_traVeFalse() throws InterruptedException {
        JwtService jwtService = newService(1);

        String token = jwtService.generateToken("khoi123", "NhanVien");
        Thread.sleep(10);

        assertThat(jwtService.isTokenValid(token, "khoi123")).isFalse();
    }

    @Test
    void extractExpiration_traVeThoiDiemSauHienTai() {
        JwtService jwtService = newService(60_000);

        String token = jwtService.generateToken("khoi123", "NhanVien");

        assertThat(jwtService.extractExpiration(token)).isAfter(new java.util.Date());
    }

    @Test
    void isTokenValid_voiTokenRac_traVeFalse() {
        JwtService jwtService = newService(60_000);

        assertThat(jwtService.isTokenValid("token-khong-hop-le", "khoi123")).isFalse();
    }
}
