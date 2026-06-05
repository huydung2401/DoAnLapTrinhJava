package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.MaGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MaGiamGiaRepository extends JpaRepository<MaGiamGia, Integer> {
    @Query("SELECT m FROM MaGiamGia m WHERE m.MaCode = :maCode")
    MaGiamGia findByMaCode(@Param("maCode") String maCode);
}
