package com.store.backend.service;

import com.store.backend.dto.LoginRequest;
import com.store.backend.dto.RegisterRequest;
import com.store.backend.model.NhanVien;
import com.store.backend.repository.NhanVienRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final NhanVienRepository nhanVienRepository;

    private final String codeErrorAccountNotMath = "Tài khoản và mật khẩu không được để trống";

    AuthService(NhanVienRepository nhanVienRepository) {
        this.nhanVienRepository = nhanVienRepository;
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
        // Gợi ý: Khi làm bản hoàn chỉnh nên mã hóa mật khẩu bằng BCryptPasswordEncoder
        nv.setMatKhau(request.getMatKhau());
        nv.setChucVu(request.getChucVu() != null ? request.getChucVu() : "NhanVien");
        nv.setTrangThai(true);

        NhanVien saved = nhanVienRepository.save(nv);

        response.put("success", true);
        response.put("message", "Đăng ký thành công");
        return response;
    }

    public Map<String, Object> login(LoginRequest request) {
        Map<String, Object> response = new HashMap<>();

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

        if (!nv.getMatKhau().equals(request.getMatKhau())) {
            response.put("success", false);
            response.put("message", "Mật khẩu không chính xác");
            return response;
        }

        nv.setMatKhau(null); // Ẩn mật khẩu trước khi trả về
        response.put("success", true);
        response.put("message", "Đăng nhập thành công");
        response.put("user", nv);
        return response;
    }
}