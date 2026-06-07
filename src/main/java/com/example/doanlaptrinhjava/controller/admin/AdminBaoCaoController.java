package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.service.BaoCaoService;
import com.example.doanlaptrinhjava.service.DonHangService;
import com.example.doanlaptrinhjava.service.NguoiDungService;
import com.example.doanlaptrinhjava.utils.AdminAuthUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;

@Controller
@RequestMapping("/admin/baocao")
public class AdminBaoCaoController {

    @Autowired
    private BaoCaoService baoCaoService;

    @Autowired
    private DonHangService donHangService;

    @Autowired
    private NguoiDungService nguoiDungService;

    /**
     * Trang báo cáo doanh thu chính
     */
    @GetMapping
    public String index(HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        int namHienTai = Year.now().getValue();

        // Dữ liệu mặc định: doanh thu theo tháng của năm hiện tại
        List<Double> doanhThuThang = baoCaoService.getDoanhThuTheoThang(namHienTai);
        Double tongDoanhThu = baoCaoService.getTongDoanhThu();
        Long tongDonHoanThanh = baoCaoService.getTongDonHoanThanh();
        Long tongKhachHang = nguoiDungService.getTongKhachHang();

        // Labels tháng 1..12
        List<String> labelsThang = new ArrayList<>();
        for (int i = 1; i <= 12; i++) labelsThang.add("Tháng " + i);

        // Danh sách năm có trong dữ liệu (cho combobox)
        Map<String, Object> dataTheoNam = baoCaoService.getDoanhThuTheoNam();
        @SuppressWarnings("unchecked")
        List<String> danhSachNam = (List<String>) dataTheoNam.get("labels");

        model.addAttribute("namHienTai", namHienTai);
        model.addAttribute("doanhThuThang", doanhThuThang);
        model.addAttribute("labelsThang", labelsThang);
        model.addAttribute("tongDoanhThu", tongDoanhThu);
        model.addAttribute("tongDonHoanThanh", tongDonHoanThanh);
        model.addAttribute("tongKhachHang", tongKhachHang);
        model.addAttribute("danhSachNam", danhSachNam);
        return "admin/baocao/index";
    }

    /**
     * API: Lấy dữ liệu doanh thu theo tháng trong năm (AJAX)
     */
    @GetMapping("/data/thang")
    @ResponseBody
    public ResponseEntity<?> dataTheoThang(@RequestParam int nam, HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");

        List<Double> data = baoCaoService.getDoanhThuTheoThang(nam);
        List<String> labels = new ArrayList<>();
        for (int i = 1; i <= 12; i++) labels.add("Tháng " + i);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        return ResponseEntity.ok(result);
    }

    /**
     * API: Lấy dữ liệu doanh thu theo năm (AJAX)
     */
    @GetMapping("/data/nam")
    @ResponseBody
    public ResponseEntity<?> dataTheoNam(HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(baoCaoService.getDoanhThuTheoNam());
    }

    /**
     * API: Lấy dữ liệu doanh thu theo khoảng ngày (AJAX)
     */
    @GetMapping("/data/ngay")
    @ResponseBody
    public ResponseEntity<?> dataTheoNgay(@RequestParam String tuNgay,
                                           @RequestParam String denNgay,
                                           HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");
        try {
            LocalDate from = LocalDate.parse(tuNgay);
            LocalDate to = LocalDate.parse(denNgay);
            return ResponseEntity.ok(baoCaoService.getDoanhThuTheoKhoangNgay(from, to));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ngày không hợp lệ");
        }
    }
}
