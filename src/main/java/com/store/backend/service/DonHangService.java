
package com.store.backend.service;

import com.store.backend.model.DonHang;
import com.store.backend.repository.DonHangRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DonHangService {

    private final DonHangRepository donHangRepository;

    DonHangService(DonHangRepository donHangRepository) {
        this.donHangRepository = donHangRepository;
    }

    // 1. Lấy tất cả đơn hàng
    public List<DonHang> getAll() {
        return donHangRepository.findAll();
    }

    // 2. Lấy đơn hàng theo mã
    public DonHang getById(Integer id) {
        return donHangRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Không tìm thấy đơn hàng: " + id));
    }

    // 3. Thêm đơn hàng
    public DonHang save(DonHang donHang) {
        return donHangRepository.save(donHang);
    }

    // 4. Cập nhật đơn hàng
    public DonHang update(Integer id, DonHang donHang) {

        DonHang existing = getById(id);

        existing.setMaNV(donHang.getMaNV());
        existing.setNgayDat(donHang.getNgayDat());
        existing.setTongTien(donHang.getTongTien());
        existing.setPhuongThucThanhToan(
                donHang.getPhuongThucThanhToan());
        existing.setTrangThai(donHang.getTrangThai());

        return donHangRepository.save(existing);
    }

    // 5. Xóa đơn hàng
    public void delete(Integer id) {
        DonHang donHang = getById(id);
        donHangRepository.delete(donHang);
    }
}
