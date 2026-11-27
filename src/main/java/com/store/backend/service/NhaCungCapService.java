
package com.store.backend.service;

import com.store.backend.model.NhaCungCap;
import com.store.backend.repository.NhaCungCapRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NhaCungCapService {

    private final NhaCungCapRepository nhaCungCapRepository;

    NhaCungCapService(NhaCungCapRepository nhaCungCapRepository) {
        this.nhaCungCapRepository = nhaCungCapRepository;
    }

    // Lấy tất cả nhà cung cấp
    public List<NhaCungCap> getAll() {
        return nhaCungCapRepository.findAll();
    }

    // Lấy nhà cung cấp theo mã
    public NhaCungCap getById(Integer id) {
        return nhaCungCapRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Không tìm thấy nhà cung cấp: " + id));
    }

    // Thêm nhà cung cấp
    public NhaCungCap save(NhaCungCap nhaCungCap) {
        return nhaCungCapRepository.save(nhaCungCap);
    }

    // Cập nhật nhà cung cấp
    public NhaCungCap update(Integer id, NhaCungCap nhaCungCap) {
        NhaCungCap existing = getById(id);

        existing.setTenNCC(nhaCungCap.getTenNCC());
        existing.setDienThoai(nhaCungCap.getDienThoai());
        existing.setEmail(nhaCungCap.getEmail());
        existing.setDiaChi(nhaCungCap.getDiaChi());
        existing.setTrangThai(nhaCungCap.getTrangThai());

        return nhaCungCapRepository.save(existing);
    }

    // Xóa nhà cung cấp
    public void delete(Integer id) {
        NhaCungCap nhaCungCap = getById(id);
        nhaCungCapRepository.delete(nhaCungCap);
    }
}
