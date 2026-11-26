
package com.store.backend.service;

import com.store.backend.model.DanhMuc;
import com.store.backend.repository.DanhMucRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DanhMucService {

    private final DanhMucRepository danhMucRepository;

    DanhMucService(DanhMucRepository danhMucRepository) {
        this.danhMucRepository = danhMucRepository;
    }

    // Lấy tất cả danh mục
    public List<DanhMuc> getAll() {
        return danhMucRepository.findAll();
    }

    // Lấy danh mục theo mã
    public DanhMuc getById(Integer id) {
        return danhMucRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Không tìm thấy danh mục: " + id));
    }

    // Thêm danh mục
    public DanhMuc save(DanhMuc danhMuc) {
        return danhMucRepository.save(danhMuc);
    }

    // Cập nhật danh mục
    public DanhMuc update(Integer id, DanhMuc danhMuc) {
        DanhMuc existing = getById(id);

        existing.setTenDM(danhMuc.getTenDM());
        existing.setMoTa(danhMuc.getMoTa());

        return danhMucRepository.save(existing);
    }

    // Xóa danh mục
    public void delete(Integer id) {
        DanhMuc danhMuc = getById(id);
        danhMucRepository.delete(danhMuc);
    }
}
