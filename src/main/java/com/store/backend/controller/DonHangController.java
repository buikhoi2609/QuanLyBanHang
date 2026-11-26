
package com.store.backend.controller;

import com.store.backend.model.DonHang;
import com.store.backend.service.DonHangService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/donhang")
@CrossOrigin(origins = "*")
public class DonHangController {

    private final DonHangService donHangService;

    DonHangController(DonHangService donHangService) {
        this.donHangService = donHangService;
    }

    // 1. Lấy tất cả đơn hàng
    @GetMapping
    public List<DonHang> getAll() {
        return donHangService.getAll();
    }

    // 2. Lấy đơn hàng theo mã
    @GetMapping("/{id}")
    public DonHang getById(@PathVariable Integer id) {
        return donHangService.getById(id);
    }

    // 3. Thêm đơn hàng
    @PostMapping
    public DonHang create(@RequestBody DonHang donHang) {
        return donHangService.save(donHang);
    }

    // 4. Cập nhật đơn hàng
    @PutMapping("/{id}")
    public DonHang update(
            @PathVariable Integer id,
            @RequestBody DonHang donHang) {

        return donHangService.update(id, donHang);
    }

    // 5. Xóa đơn hàng
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        donHangService.delete(id);
        return "Xóa đơn hàng thành công!";
    }
}
