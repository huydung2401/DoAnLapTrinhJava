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
}