package com.store.backend.repository;

import com.store.backend.model.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {

    Optional<NhanVien> findByTaiKhoan(String taiKhoan);

    boolean existsByTaiKhoan(String taiKhoan);
}