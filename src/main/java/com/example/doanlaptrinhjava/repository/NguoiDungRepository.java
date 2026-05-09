package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {
    NguoiDung findByEmailAndMatKhau(
            String email,
            String matKhau
    );

    boolean existsByEmail(String email);
}