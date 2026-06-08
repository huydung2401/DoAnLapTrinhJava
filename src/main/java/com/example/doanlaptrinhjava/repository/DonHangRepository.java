package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.query.Param;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {

    @Query(value = "SELECT COUNT(*) FROM DonHang WHERE TrangThai = 'CHO_XAC_NHAN'", nativeQuery = true)
    Long countDonHangMoi();

    @Query(value = "SELECT COALESCE(SUM(TongTien), 0) FROM DonHang WHERE TrangThai = 'HOAN_THANH' AND DATE(NgayDat) = CURDATE()", nativeQuery = true)
    Double getDoanhThuHomNay();

    @Query(value = "SELECT DATE(NgayDat) as ngay, SUM(TongTien) as doanhThu " +
            "FROM DonHang " +
            "WHERE TrangThai = 'HOAN_THANH' AND NgayDat >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
            "GROUP BY DATE(NgayDat) " +
            "ORDER BY ngay ASC", nativeQuery = true)
    List<Object[]> getDoanhThu7NgayQua();

    List<DonHang> findByNguoiDung_IdNguoiDungOrderByNgayDatDesc(
            Integer idNguoiDung
    );

    @Query(value = """
            SELECT COUNT(*)
            FROM DonHang dh
            INNER JOIN ChiTietDonHang ct
                    ON dh.idDonHang = ct.IdDonHang
            WHERE dh.idNguoiDung = :idNguoiDung
              AND ct.IdSanPham = :idSanPham
              AND dh.TrangThai = 'DA_THANH_TOAN'
            """, nativeQuery = true)
    long daMuaSanPham(
            @Param("idNguoiDung") Integer idNguoiDung,
            @Param("idSanPham") Integer idSanPham
    );

    // Sửa query JPQL đúng field ngayDat
    @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE d.ngayDat >= :tuNgay")
    Double tinhDoanhThuTuNgay(@Param("tuNgay") LocalDateTime tuNgay);

    long countByTrangThai(String trangThai);

    List<DonHang> findTop5ByOrderByNgayDatDesc();

    @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE FUNCTION('MONTH', d.ngayDat) = FUNCTION('MONTH', CURRENT_DATE)")
    Double tinhDoanhThuThangHienTai();

    List<DonHang> findByTrangThai(String trangThai);

    @Query("SELECT SUM(d.tongTien) FROM DonHang d WHERE d.ngayDat BETWEEN :tuNgay AND :denNgay AND d.trangThai = 'DA_THANH_TOAN'")
    Double tinhDoanhThuTheoKhoangThoiGian(
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay
    );

    @Query("SELECT COUNT(d) FROM DonHang d WHERE d.ngayDat BETWEEN :tuNgay AND :denNgay AND d.trangThai = 'DA_THANH_TOAN'")
    Long countDonHangTheoKhoangThoiGian(
            @Param("tuNgay") LocalDateTime tuNgay,
            @Param("denNgay") LocalDateTime denNgay
    );

    @Query(value = """
            SELECT
                MONTH(NgayDat) as thang,
                YEAR(NgayDat) as nam,
                COUNT(*) as soDon,
                SUM(TongTien) as tongTien
            FROM DonHang
            WHERE TrangThai = 'DA_THANH_TOAN'
            GROUP BY YEAR(NgayDat), MONTH(NgayDat)
            ORDER BY nam, thang
            """, nativeQuery = true)
    List<Object[]> getDoanhThuTheoThang();

    @Query(value = """
            SELECT DATE(NgayDat) as ngay,
                   COUNT(*) as soDon,
                   SUM(TongTien) as tongTien
            FROM DonHang
            WHERE TrangThai = 'DA_THANH_TOAN'
            GROUP BY DATE(NgayDat)
            ORDER BY ngay
            """, nativeQuery = true)
    List<Object[]> getDoanhThuTheoNgay();

    @Query(value = """
            SELECT
                DATE(NgayDat) as ngay,
                COUNT(*) as soDon,
                SUM(TongTien) as tongTien
            FROM DonHang
            WHERE TrangThai = 'DA_THANH_TOAN'
              AND NgayDat >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)
            GROUP BY DATE(NgayDat)
            ORDER BY ngay ASC
            """, nativeQuery = true)
    List<Object[]> getDoanhThu7Ngay();
}