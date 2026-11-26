
package com.store.backend.controller;

import com.store.backend.model.DanhMuc;
import com.store.backend.service.DanhMucService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/danhmuc")
@CrossOrigin(origins = "*")
public class DanhMucController {

    private final DanhMucService danhMucService;

    DanhMucController(DanhMucService danhMucService) {
        this.danhMucService = danhMucService;
    }

    // Lấy tất cả danh mục
    @GetMapping
    public List<DanhMuc> getAll() {
        return danhMucService.getAll();
    }

    // Lấy danh mục theo ID
    @GetMapping("/{id}")
    public DanhMuc getById(@PathVariable Integer id) {
        return danhMucService.getById(id);
    }

    // Thêm danh mục
    @PostMapping
    public DanhMuc create(@RequestBody DanhMuc danhMuc) {
        return danhMucService.save(danhMuc);
    }

    // Cập nhật danh mục
    @PutMapping("/{id}")
    public DanhMuc update(
            @PathVariable Integer id,
            @RequestBody DanhMuc danhMuc) {
        return danhMucService.update(id, danhMuc);
    }

    // Xóa danh mục
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        danhMucService.delete(id);
        return "Xóa danh mục thành công!";
    }
}
