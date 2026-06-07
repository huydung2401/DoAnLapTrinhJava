package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    NguoiDung findByEmailAndMatKhau(
            String email,
            String matKhau
    );

    boolean existsByEmail(String email);

    // === Admin: Quản lý Khách hàng ===
    List<NguoiDung> findByVaiTroOrderByNgayTaoDesc(String vaiTro);

    List<NguoiDung> findByHoTenContainingIgnoreCaseAndVaiTro(String keyword, String vaiTro);

    Long countByVaiTro(String vaiTro);
}