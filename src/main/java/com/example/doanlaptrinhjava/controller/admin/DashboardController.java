package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.model.DonHang;
import com.example.doanlaptrinhjava.model.HoatDongGanDay;
import com.example.doanlaptrinhjava.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {

        // ===== 4 THẺ THỐNG KÊ =====

        // Tổng sản phẩm
        long tongSanPham = sanPhamRepository.count();
        model.addAttribute("tongSanPham", tongSanPham);

        // Sản phẩm sắp hết (tồn kho < 10)
        long sanPhamSapHet = sanPhamRepository.countBySoLuongTonLessThan(10);
        model.addAttribute("sanPhamSapHet", sanPhamSapHet);

        // Tổng doanh thu tháng hiện tại
        LocalDateTime dauThang = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        Double tongDoanhThu = donHangRepository.tinhDoanhThuTuNgay(dauThang);
        model.addAttribute("tongDoanhThu", tongDoanhThu != null ? tongDoanhThu : 0.0);

        // Đơn hàng mới (trạng thái chờ xử lý)
        long donHangMoi = donHangRepository.countByTrangThai("CHO_XAC_NHAN");
        model.addAttribute("donHangMoi", donHangMoi);

        // Tổng khách hàng
        long tongKhachHang = khachHangRepository.count();
        model.addAttribute("tongKhachHang", tongKhachHang);

        // ===== HOẠT ĐỘNG GẦN ĐÂY =====
        List<HoatDongGanDay> hoatDongList = buildHoatDong();
        model.addAttribute("hoatDongGanDay", hoatDongList);

        return "admin/dashboard/index";
    }

    // Tạo danh sách hoạt động gần đây từ nhiều nguồn
    private List<HoatDongGanDay> buildHoatDong() {
        List<HoatDongGanDay> list = new ArrayList<>();

        // Lấy 5 đơn hàng mới nhất
        donHangRepository.findTop5ByOrderByNgayDatDesc().forEach(dh ->
                list.add(new HoatDongGanDay(
                        "Đơn hàng #" + dh.getIdDonHang() + " - " + dh.getTrangThai(),
                        "fa-shopping-cart",
                        dh.getNgayDat()
                ))
        );

        // Lấy 3 sản phẩm mới nhất
        sanPhamRepository.findTop3ByOrderByNgayThemDesc().forEach(sp ->
                list.add(new HoatDongGanDay(
                        "Sản phẩm mới: " + sp.getTenSanPham(),
                        "fa-box",
                        sp.getNgayThem()
                ))
        );

        // Sắp xếp theo thời gian mới nhất
        list.sort((a, b) -> b.getThoiGian().compareTo(a.getThoiGian()));

        return list;

    }
}