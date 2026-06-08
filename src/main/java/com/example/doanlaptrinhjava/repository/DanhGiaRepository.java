package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Integer> {

    @Query("""
            SELECT d
            FROM DanhGia d
            WHERE d.sanPham.idSanPham = :idSanPham
            AND d.daDuyet = true
            ORDER BY d.ngayDanhGia DESC
            """)
    List<DanhGia> findBySanPham_IdSanPhamAndDaDuyetTrueOrderByNgayDanhGiaDesc(
            @Param("idSanPham") Integer idSanPham);

    @Query("""
            SELECT AVG(d.soSao)
            FROM DanhGia d
            WHERE d.sanPham.idSanPham = :idSanPham
            AND d.daDuyet = true
            """)
    Double getAverageRating(
            @Param("idSanPham") Integer idSanPham);

    @Query("""
            SELECT COUNT(d)
            FROM DanhGia d
            WHERE d.sanPham.idSanPham = :idSanPham
            AND d.daDuyet = true
            """)
    Long countBySanPham_IdSanPhamAndDaDuyetTrue(
            @Param("idSanPham") Integer idSanPham);

    @Query("""
            SELECT d
            FROM DanhGia d
            ORDER BY d.ngayDanhGia DESC
            """)
    List<DanhGia> findAllOrderByNgayDanhGiaDesc();

    @Query("""
            SELECT d
            FROM DanhGia d
            WHERE d.daDuyet = :daDuyet
            ORDER BY d.ngayDanhGia DESC
            """)
    List<DanhGia> findByDaDuyetOrderByNgayDanhGiaDesc(
            @Param("daDuyet") Boolean daDuyet);

    @Query("""
            SELECT COUNT(d)
            FROM DanhGia d
            WHERE d.daDuyet = false
            """)
    Long countDanhGiaChoDuyet();

    @Query("""
            SELECT AVG(d.soSao)
            FROM DanhGia d
            WHERE d.sanPham.idSanPham = :idSanPham
            AND d.daDuyet = true
            """)
    Double getAvgRating(
            @Param("idSanPham") Integer idSanPham);

    @Query("""
            SELECT COUNT(d)
            FROM DanhGia d
            WHERE d.sanPham.idSanPham = :idSanPham
            AND d.daDuyet = true
            """)
    Long getTotalReviews(
            @Param("idSanPham") Integer idSanPham);
}