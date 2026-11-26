
package com.store.backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "DonHang")
@Data
public class DonHang {

    // Mã đơn hàng
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaDH")
    private Integer maDH;

    // Mã nhân viên
    @Column(name = "MaNV")
    private Integer maNV;

    // Ngày đặt hàng
    @Column(name = "NgayDat")
    private LocalDateTime ngayDat = LocalDateTime.now();

    // Tổng tiền đơn hàng
    @Column(name = "TongTien", precision = 18, scale = 2)
    private BigDecimal tongTien;

    // Phương thức thanh toán
    @Column(name = "PhuongThucThanhToan")
    private String phuongThucThanhToan;

    // Trạng thái đơn hàng
    @Column(name = "TrangThai")
    private String trangThai = "Hoàn thành";
}
