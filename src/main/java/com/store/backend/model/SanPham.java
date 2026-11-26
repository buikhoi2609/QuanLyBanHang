package com.store.backend.model;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "SanPham")
@Data
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaSP")
    private Integer maSP;

    @Column(name = "TenSP", nullable = false)
    private String tenSP;

    @Column(name = "MaDM", nullable = false)
    private Integer maDM;

    @Column(name = "MaNCC")
    private Integer maNCC;

    @Column(name = "DonViTinh")
    private String donViTinh;

    @Column(name = "GiaNhap", nullable = false)
    private BigDecimal giaNhap;

    @Column(name = "GiaBan", nullable = false)
    private BigDecimal giaBan;

    @Column(name = "SoLuongTon", nullable = false)
    private Integer soLuongTon;

    @Column(name = "SoLuongCanhBao")
    private Integer soLuongCanhBao;

    @Column(name = "HinhAnh")
    private String hinhAnh;

    @Column(name = "MoTa",columnDefinition = "NVARCHAR(MAX)")
    private String moTa;

    @Column(name = "TrangThai")
    private Boolean trangThai = true;
}
