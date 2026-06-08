package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.NguoiDung; // Dùng model NguoiDung của bạn
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KhachHangRepository extends JpaRepository<NguoiDung, Integer> {
    // Nếu bạn muốn đếm theo vai trò (ví dụ: chỉ khách hàng, không phải admin)
    long countByVaiTro(String vaiTro);
}