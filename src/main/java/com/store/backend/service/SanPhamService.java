
package com.store.backend.service;

import com.store.backend.model.SanPham;
import com.store.backend.repository.SanPhamRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamService {

    private final SanPhamRepository sanPhamRepository;

    public SanPhamService(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }

    // Lấy tất cả sản phẩm
    public List<SanPham> getAll() {
        return sanPhamRepository.findAll();
    }

    // Lấy sản phẩm theo mã
    public SanPham getById(Integer id) {
        return sanPhamRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy sản phẩm có mã: " + id));
    }

    // Thêm sản phẩm
    public SanPham save(SanPham sanPham) {
        return sanPhamRepository.save(sanPham);
    }

    // Cập nhật sản phẩm
    public SanPham update(Integer id, SanPham sanPham) {
        SanPham existing = getById(id);

        existing.setTenSP(sanPham.getTenSP());
        existing.setMaDM(sanPham.getMaDM());
        existing.setMaNCC(sanPham.getMaNCC());
        existing.setDonViTinh(sanPham.getDonViTinh());
        existing.setGiaNhap(sanPham.getGiaNhap());
        existing.setGiaBan(sanPham.getGiaBan());
        existing.setSoLuongTon(sanPham.getSoLuongTon());
        existing.setSoLuongCanhBao(sanPham.getSoLuongCanhBao());
        existing.setHinhAnh(sanPham.getHinhAnh());
        existing.setMoTa(sanPham.getMoTa());
        existing.setTrangThai(sanPham.getTrangThai());

        return sanPhamRepository.save(existing);
    }

    // Xóa sản phẩm
    public void delete(Integer id) {
        SanPham sanPham = getById(id);
        sanPhamRepository.delete(sanPham);
    }
}
