package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.ChiTietGioHang;

import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ChiTietGioHangRepository
        extends JpaRepository<ChiTietGioHang, Integer> {

    List<ChiTietGioHang> findByGioHang_IdGioHang(Integer idGioHang);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM ChiTietGioHang WHERE IdChiTietGioHang = :id",
            nativeQuery = true
    )
    void xoaTheoId(@Param("id") Integer id);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM ChiTietGioHang WHERE IdGioHang = :idGioHang",
            nativeQuery = true
    )
    void xoaTheoGioHang(@Param("idGioHang") Integer idGioHang);
}