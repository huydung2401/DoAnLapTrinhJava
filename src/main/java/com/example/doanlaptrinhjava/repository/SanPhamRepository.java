package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    @Query("SELECT s FROM SanPham s WHERE LOWER(s.tenSanPham) LIKE LOWER(CONCAT('%', :tenSanPham, '%'))")
    List<SanPham> findByTenSanPhamContainingIgnoreCase(@Param("tenSanPham") String tenSanPham);

    @Query("SELECT s FROM SanPham s WHERE LOWER(s.tenSanPham) LIKE LOWER(CONCAT('%', :tenSanPham, '%')) AND s.danhMuc.IdDanhMuc = :idDanhMuc")
    List<SanPham> findByTenSanPhamContainingIgnoreCaseAndDanhMuc_IdDanhMuc(@Param("tenSanPham") String tenSanPham, @Param("idDanhMuc") Integer idDanhMuc);

    @Query("SELECT s FROM SanPham s WHERE s.danhMuc.IdDanhMuc = :idDanhMuc")
    List<SanPham> findByDanhMuc_IdDanhMuc(@Param("idDanhMuc") Integer idDanhMuc);

    @Query("SELECT s FROM SanPham s WHERE s.giaKhuyenMai IS NOT NULL")
    List<SanPham> findByGiaKhuyenMaiIsNotNull();

    @Query("SELECT s FROM SanPham s WHERE s.giaKhuyenMai IS NULL")
    List<SanPham> findByGiaKhuyenMaiIsNull();

    @Query("SELECT COUNT(s) FROM SanPham s WHERE s.soLuongTon <= 10")
    Long countSpSapHetHang();

    @Query(value = "SELECT * FROM SanPham ORDER BY daBan DESC LIMIT 5", nativeQuery = true)
    List<SanPham> findTop5BanChay();

    @Query("SELECT s FROM SanPham s WHERE LOWER(s.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) AND s.trangThai = true")
    List<SanPham> findByTenSanPhamContainingIgnoreCaseAndTrangThaiTrue(@Param("keyword") String keyword);

    @Query("SELECT s FROM SanPham s WHERE s.danhMuc.IdDanhMuc = :categoryId AND s.trangThai = true")
    List<SanPham> findByDanhMuc_IdDanhMucAndTrangThaiTrue(@Param("categoryId") Integer categoryId);

    @Query("SELECT s FROM SanPham s WHERE s.danhMuc.IdDanhMuc = :categoryId AND LOWER(s.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) AND s.trangThai = true")
    List<SanPham> findByDanhMuc_IdDanhMucAndTenSanPhamContainingIgnoreCaseAndTrangThaiTrue(@Param("categoryId") Integer categoryId, @Param("keyword") String keyword);

    long countBySoLuongTonLessThan(int soLuong);

    List<SanPham> findTop3ByOrderByNgayThemDesc();

    @Query(value = """
            SELECT *
            FROM SanPham
            WHERE TrangThai = 1
            AND (
                LOWER(TenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(MoTaNgan) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(MoTaChiTiet) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """, nativeQuery = true)
    List<SanPham> timKiemNangCao(@Param("keyword") String keyword);
}