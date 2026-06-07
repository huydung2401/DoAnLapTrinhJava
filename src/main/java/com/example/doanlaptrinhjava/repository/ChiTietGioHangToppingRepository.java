package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.ChiTietGioHangTopping;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChiTietGioHangToppingRepository
        extends JpaRepository<ChiTietGioHangTopping, Integer> {

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM ChiTietGioHang_Topping WHERE IdChiTietGioHang = :id",
            nativeQuery = true
    )
    void xoaTheoChiTietGioHang(@Param("id") Integer id);

}