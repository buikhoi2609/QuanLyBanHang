package com.store.backend.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String hoTen;
    private String dienThoai;
    private String email;
    private String taiKhoan;
    private String matKhau;
    private String chucVu;
}