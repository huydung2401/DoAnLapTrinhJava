package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.MaGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MaGiamGiaRepository
        extends JpaRepository<MaGiamGia, Integer> {

    @Query("""
            SELECT m
            FROM MaGiamGia m
            WHERE m.MaCode = :maCode
            """)
    MaGiamGia findByMaCode(
            @Param("maCode") String maCode
    );

    @Query("""
            SELECT m
            FROM MaGiamGia m
            WHERE m.KichHoat = true
            AND CURRENT_TIMESTAMP BETWEEN m.NgayBatDau
            AND m.NgayKetThuc
            AND m.DaSuDung < m.SoLuong
            ORDER BY m.NgayKetThuc ASC
            """)
    List<MaGiamGia> findVoucherConHan();
}