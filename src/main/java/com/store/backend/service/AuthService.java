package com.store.backend.service;

import com.store.backend.dto.LoginRequest;
import com.store.backend.dto.RegisterRequest;
import com.store.backend.model.NhanVien;
import com.store.backend.repository.NhanVienRepository;
import com.store.backend.security.JwtService;
import com.store.backend.security.TokenBlacklistService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final NhanVienRepository nhanVienRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    private final String codeErrorAccountNotMath = "Tài khoản và mật khẩu không được để trống";

    public AuthService(NhanVienRepository nhanVienRepository, PasswordEncoder passwordEncoder,
                        JwtService jwtService, TokenBlacklistService tokenBlacklistService) {
        this.nhanVienRepository = nhanVienRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    public Map<String, Object> register(RegisterRequest request) {
        Map<String, Object> response = new HashMap<>();

        if (request.getTaiKhoan() == null || request.getTaiKhoan().isBlank()
                || request.getMatKhau() == null || request.getMatKhau().isBlank()) {
            response.put("success", false);
            response.put("message", codeErrorAccountNotMath);
            return response;
        }

        if (nhanVienRepository.existsByTaiKhoan(request.getTaiKhoan())) {
            response.put("success", false);
            response.put("message", "Tên tài khoản đã tồn tại");
            return response;
        }

        NhanVien nv = new NhanVien();
        nv.setHoTen(request.getHoTen());
        nv.setDienThoai(request.getDienThoai());
        nv.setEmail(request.getEmail());
        nv.setTaiKhoan(request.getTaiKhoan());
        nv.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        nv.setChucVu(request.getChucVu() != null ? request.getChucVu() : "NhanVien");
        nv.setTrangThai(true);

        nhanVienRepository.save(nv);

        response.put("success", true);
        response.put("message", "Đăng ký thành công");
        return response;
    }

    public Map<String, Object> login(LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

        if (request.getTaiKhoan() == null || request.getTaiKhoan().isBlank()
                || request.getMatKhau() == null || request.getMatKhau().isBlank()) {
            response.put("success", false);
            response.put("message", codeErrorAccountNotMath);
            return response;
        }

        Optional<NhanVien> optionalNV = nhanVienRepository.findByTaiKhoan(request.getTaiKhoan());
        if (optionalNV.isEmpty()) {
            response.put("success", false);
            response.put("message", "Tài khoản không tồn tại");
            return response;
        }

        NhanVien nv = optionalNV.get();
        if (Boolean.FALSE.equals(nv.getTrangThai())) {
            response.put("success", false);
            response.put("message", "Tài khoản đã bị khóa");
            return response;
        }

        if (!passwordEncoder.matches(request.getMatKhau(), nv.getMatKhau())) {
            response.put("success", false);
            response.put("message", "Mật khẩu không chính xác");
            return response;
        }

        String token = jwtService.generateToken(nv.getTaiKhoan(), nv.getChucVu());

        nv.setMatKhau(null); // Ẩn mật khẩu trước khi trả về
        response.put("success", true);
        response.put("message", "Đăng nhập thành công");
        response.put("token", token);
        response.put("user", nv);
        return response;
    }

    public Map<String, Object> logout(String token) {
        Map<String, Object> response = new HashMap<>();

        if (token != null && !token.isBlank()) {
            try {
                Date expiration = jwtService.extractExpiration(token);
                tokenBlacklistService.blacklist(token, expiration.toInstant());
            } catch (Exception ignored) {
                // Token không hợp lệ/đã hết hạn: không cần chặn thêm, vẫn coi như đăng xuất thành công
            }
        }

        response.put("success", true);
        response.put("message", "Đăng xuất thành công");
        return response;
    }

    public Map<String, Object> getProfile(String taiKhoan) {
        Map<String, Object> response = new HashMap<>();

        Optional<NhanVien> optionalNV = nhanVienRepository.findByTaiKhoan(taiKhoan);
        if (optionalNV.isEmpty()) {
            response.put("success", false);
            response.put("message", "Không tìm thấy người dùng");
            return response;
        }

        NhanVien nv = optionalNV.get();
        nv.setMatKhau(null);
        response.put("success", true);
        response.put("user", nv);
        return response;
    }
}
