package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.DonHangMaGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonHangMaGiamGiaRepository extends JpaRepository<DonHangMaGiamGia, Integer> {
}
