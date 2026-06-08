package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.MaGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MaGiamGiaRepository extends JpaRepository<MaGiamGia, Integer> {

    Optional<MaGiamGia> findByMaCode(String maCode);

    @Query("""
            SELECT m
            FROM MaGiamGia m
            WHERE m.kichHoat = true
              AND m.ngayBatDau <= CURRENT_TIMESTAMP
              AND m.ngayKetThuc >= CURRENT_TIMESTAMP
              AND m.daSuDung < m.soLuong
            ORDER BY m.ngayKetThuc ASC
            """)
    List<MaGiamGia> findVoucherConHan();
}