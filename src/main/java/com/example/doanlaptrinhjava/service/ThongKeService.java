package com.example.doanlaptrinhjava.service;

import com.example.doanlaptrinhjava.model.HoatDongGanDay;
import com.example.doanlaptrinhjava.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThongKeService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    // Các hàm thống kê cơ bản
    public long getTongSanPham() {
        return sanPhamRepository.count();
    }

    public long getSanPhamSapHet() {
        return sanPhamRepository.countBySoLuongTonLessThan(10);
    }

    public Double getTongDoanhThuThang() {
        Double dt = donHangRepository.tinhDoanhThuThangHienTai();
        return dt != null ? dt : 0.0;
    }

    public long getDonHangMoi() {
        return donHangRepository.countByTrangThai("Chờ xử lý");
    }

    public long getTongKhachHang() {
        return khachHangRepository.count();
    }

    // Hàm lấy danh sách hoạt động gần đây
    public List<HoatDongGanDay> getHoatDongGanDay() {
        List<HoatDongGanDay> list = new ArrayList<>();

        // Lấy dữ liệu từ Repository và map vào class HoatDongGanDay
        // Ví dụ:
        donHangRepository.findTop5ByOrderByNgayDatDesc().forEach(dh ->
                list.add(new HoatDongGanDay(
                        "Đơn hàng #" + dh.getIdDonHang() + " vừa đặt",
                        "fa-shopping-cart",
                        dh.getNgayDat()
                ))
        );

        return list;
    }
}