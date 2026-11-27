
package com.store.backend.controller;

import com.store.backend.model.NhaCungCap;
import com.store.backend.service.NhaCungCapService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/nhacungcap")
@CrossOrigin(origins = "*")
public class NhaCungCapController {

    private final NhaCungCapService nhaCungCapService;

    NhaCungCapController(NhaCungCapService nhaCungCapService) {
        this.nhaCungCapService = nhaCungCapService;
    }

    // Lấy tất cả nhà cung cấp
    @GetMapping
    public List<NhaCungCap> getAll() {
        return nhaCungCapService.getAll();
    }

    // Lấy nhà cung cấp theo mã
    @GetMapping("/{id}")
    public NhaCungCap getById(@PathVariable Integer id) {
        return nhaCungCapService.getById(id);
    }

    // Thêm nhà cung cấp
    @PostMapping
    public NhaCungCap create(@RequestBody NhaCungCap nhaCungCap) {
        return nhaCungCapService.save(nhaCungCap);
    }

    // Cập nhật nhà cung cấp
    @PutMapping("/{id}")
    public NhaCungCap update(
            @PathVariable Integer id,
            @RequestBody NhaCungCap nhaCungCap) {
        return nhaCungCapService.update(id, nhaCungCap);
    }

    // Xóa nhà cung cấp
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        nhaCungCapService.delete(id);
        return "Xóa nhà cung cấp thành công!";
    }
}
