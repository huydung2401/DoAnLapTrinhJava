package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.ChiTietNhapKho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChiTietNhapKhoRepository extends JpaRepository<ChiTietNhapKho, Integer> {
    @Query("SELECT c FROM ChiTietNhapKho c WHERE c.phieuNhapKho.IdPhieuNhap = :idPhieuNhap")
    List<ChiTietNhapKho> findByPhieuNhapKho_IdPhieuNhap(@Param("idPhieuNhap") Integer idPhieuNhap);
}
