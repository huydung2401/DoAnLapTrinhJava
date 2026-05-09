package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.GioHang;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GioHangRepository
        extends JpaRepository<GioHang, Integer> {

    GioHang findByNguoiDung_IdNguoiDung(Integer idNguoiDung);
}