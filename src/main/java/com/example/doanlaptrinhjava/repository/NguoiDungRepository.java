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

    List<NguoiDung> findByHoTenContainingIgnoreCaseOrEmailContainingIgnoreCase(String hoTen, String email);

    List<NguoiDung> findByTrangThai(Boolean trangThai);

    List<NguoiDung>
    findByHoTenContainingIgnoreCaseOrEmailContainingIgnoreCaseOrSoDienThoaiContaining(
            String hoTen,
            String email,
            String soDienThoai
    );
}