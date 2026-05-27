package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.PhieuNhapKho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhieuNhapKhoRepository extends JpaRepository<PhieuNhapKho, Integer> {
    @Query("SELECT p FROM PhieuNhapKho p ORDER BY p.NgayNhap DESC")
    List<PhieuNhapKho> findAllByOrderByNgayNhapDesc();
}
