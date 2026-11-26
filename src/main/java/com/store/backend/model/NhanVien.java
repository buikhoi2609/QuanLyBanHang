package com.store.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "NhanVien")
@Data
public class NhanVien {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNV")
    private Integer maNV;

    @Column(name = "HoTen", nullable = false)
    private String hoTen;

    @Column(name = "DienThoai")
    private String dienThoai;

    @Column(name = "Email")
    private String email;

    @Column(name = "TaiKhoan", unique = true)
    private String taiKhoan;

    @Column(name = "MatKhau")
    private String matKhau;

    @Column(name = "ChucVu")
    private String chucVu;

    @Column(name = "TrangThai")
    private Boolean trangThai = true;
}