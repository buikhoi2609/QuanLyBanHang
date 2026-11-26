
package com.store.backend.repository;

import com.store.backend.model.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DanhMucRepository
        extends JpaRepository<DanhMuc, Integer> {
}
