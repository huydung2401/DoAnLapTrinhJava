package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.model.SanPham;
import com.example.doanlaptrinhjava.service.DanhGiaService;
import com.example.doanlaptrinhjava.service.DonHangService;
import com.example.doanlaptrinhjava.service.SanPhamService;
import com.example.doanlaptrinhjava.utils.AdminAuthUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class AdminDashboardController {

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private DanhGiaService danhGiaService;

    @GetMapping("/admin/dashboard")
    public String index(HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        Long donHangMoi = donHangService.getSoDonHangMoi();
        Double doanhThuHomNay = donHangService.getDoanhThuHomNay();
        Long spSapHetHang = sanPhamService.getSoSanPhamSapHetHang();
        Long danhGiaChoDuyet = danhGiaService.getSoDanhGiaChoDuyet();
        List<SanPham> top5BanChay = sanPhamService.getTop5BanChay();

        List<Object[]> doanhThu7Ngay = donHangService.getDoanhThu7NgayQua();
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

        for (Object[] row : doanhThu7Ngay) {
            Date date = (Date) row[0];
            Double dt = (Double) row[1];
            labels.add(sdf.format(date));
            data.add(dt != null ? dt : 0.0);
        }

        model.addAttribute("donHangMoi", donHangMoi);
        model.addAttribute("doanhThuHomNay", doanhThuHomNay);
        model.addAttribute("spSapHetHang", spSapHetHang);
        model.addAttribute("danhGiaChoDuyet", danhGiaChoDuyet);
        model.addAttribute("top5BanChay", top5BanChay);
        model.addAttribute("chartLabels", labels);
        model.addAttribute("chartData", data);

        return "admin/dashboard/index";
    }
}
