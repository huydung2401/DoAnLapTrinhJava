package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query("SELECT s FROM SanPham s WHERE LOWER(s.TenSanPham) LIKE LOWER(CONCAT('%', :tenSanPham, '%'))")
    List<SanPham> findByTenSanPhamContainingIgnoreCase(@Param("tenSanPham") String tenSanPham);

    @Query("SELECT s FROM SanPham s WHERE LOWER(s.TenSanPham) LIKE LOWER(CONCAT('%', :tenSanPham, '%')) AND s.danhMuc.IdDanhMuc = :idDanhMuc")
    List<SanPham> findByTenSanPhamContainingIgnoreCaseAndDanhMuc_IdDanhMuc(@Param("tenSanPham") String tenSanPham, @Param("idDanhMuc") Integer idDanhMuc);

    @Query("SELECT s FROM SanPham s WHERE s.danhMuc.IdDanhMuc = :idDanhMuc")
    List<SanPham> findByDanhMuc_IdDanhMuc(@Param("idDanhMuc") Integer idDanhMuc);

    @Query("SELECT s FROM SanPham s WHERE s.GiaKhuyenMai IS NOT NULL")
    List<SanPham> findByGiaKhuyenMaiIsNotNull();

    @Query("SELECT s FROM SanPham s WHERE s.GiaKhuyenMai IS NULL")
    List<SanPham> findByGiaKhuyenMaiIsNull();

    @Query("SELECT COUNT(s) FROM SanPham s WHERE s.SoLuongTon <= 10")
    Long countSpSapHetHang();

    @Query(value = "SELECT * FROM SanPham ORDER BY DaBan DESC LIMIT 5", nativeQuery = true)
    List<SanPham> findTop5BanChay();
}