package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.ChiTietDonHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChiTietDonHangRepository
        extends JpaRepository<ChiTietDonHang, Integer> {

    List<ChiTietDonHang> findByDonHang_IdDonHang(Integer idDonHang);
}