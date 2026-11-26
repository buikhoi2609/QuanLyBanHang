
package com.store.backend.controller;

import com.store.backend.model.SanPham;
import com.store.backend.service.SanPhamService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sanpham")
@CrossOrigin(origins = "*")
public class SanPhamController {

    private final SanPhamService sanPhamService;

    public SanPhamController(SanPhamService sanPhamService) {
        this.sanPhamService = sanPhamService;
    }

    // Lấy danh sách sản phẩm
    @GetMapping
    public List<SanPham> getAll() {
        return sanPhamService.getAll();
    }

    // Lấy sản phẩm theo mã
    @GetMapping("/{id}")
    public SanPham getById(@PathVariable Integer id) {
        return sanPhamService.getById(id);
    }

    // Thêm sản phẩm
    @PostMapping
    public SanPham create(@RequestBody SanPham sanPham) {
        return sanPhamService.save(sanPham);
    }

    // Cập nhật sản phẩm
    @PutMapping("/{id}")
    public SanPham update(
            @PathVariable Integer id,
            @RequestBody SanPham sanPham) {
        return sanPhamService.update(id, sanPham);
    }

    // Xóa sản phẩm
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        sanPhamService.delete(id);
        return "Xóa sản phẩm thành công!";
    }
}
