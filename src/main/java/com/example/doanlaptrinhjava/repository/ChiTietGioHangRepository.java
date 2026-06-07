package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.ChiTietGioHang;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChiTietGioHangRepository
        extends JpaRepository<ChiTietGioHang, Integer> {
}