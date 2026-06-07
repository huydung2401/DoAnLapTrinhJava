package com.example.doanlaptrinhjava.repository;

import com.example.doanlaptrinhjava.model.DonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    // === Admin: Quản lý Đơn hàng ===
    List<DonHang> findAllByOrderByNgayDatDesc();

    List<DonHang> findByTrangThaiOrderByNgayDatDesc(String trangThai);

    List<DonHang> findByNguoiDung_IdNguoiDungOrderByNgayDatDesc(Integer idNguoiDung);

    // === Admin: Báo cáo Doanh thu ===
    @Query(value = "SELECT MONTH(NgayDat) as thang, COALESCE(SUM(TongTien), 0) as doanhThu " +
                   "FROM DonHang " +
                   "WHERE TrangThai = 'HOAN_THANH' AND YEAR(NgayDat) = :nam " +
                   "GROUP BY MONTH(NgayDat) " +
                   "ORDER BY thang ASC", nativeQuery = true)
    List<Object[]> getDoanhThuTheoThang(int nam);

    @Query(value = "SELECT YEAR(NgayDat) as nam, COALESCE(SUM(TongTien), 0) as doanhThu " +
                   "FROM DonHang " +
                   "WHERE TrangThai = 'HOAN_THANH' " +
                   "GROUP BY YEAR(NgayDat) " +
                   "ORDER BY nam ASC", nativeQuery = true)
    List<Object[]> getDoanhThuTheoNam();

    @Query(value = "SELECT DATE(NgayDat) as ngay, COALESCE(SUM(TongTien), 0) as doanhThu " +
                   "FROM DonHang " +
                   "WHERE TrangThai = 'HOAN_THANH' AND NgayDat BETWEEN :tuNgay AND :denNgay " +
                   "GROUP BY DATE(NgayDat) " +
                   "ORDER BY ngay ASC", nativeQuery = true)
    List<Object[]> getDoanhThuTheoKhoangNgay(String tuNgay, String denNgay);

    @Query(value = "SELECT COALESCE(SUM(TongTien), 0) FROM DonHang WHERE TrangThai = 'HOAN_THANH'", nativeQuery = true)
    Double getTongDoanhThu();

    @Query(value = "SELECT COUNT(*) FROM DonHang WHERE TrangThai = 'HOAN_THANH'", nativeQuery = true)
    Long getTongDonHoanThanh();
}