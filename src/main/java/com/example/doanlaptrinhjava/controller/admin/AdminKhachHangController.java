package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.model.DonHang;
import com.example.doanlaptrinhjava.model.NguoiDung;
import com.example.doanlaptrinhjava.service.DonHangService;
import com.example.doanlaptrinhjava.service.NguoiDungService;
import com.example.doanlaptrinhjava.utils.AdminAuthUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/khachhang")
public class AdminKhachHangController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private DonHangService donHangService;

    /**
     * Danh sách khách hàng (có tìm kiếm theo tên)
     */
    @GetMapping
    public String index(@RequestParam(required = false) String keyword,
                        HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        List<NguoiDung> listKhachHang = nguoiDungService.getKhachHangList(keyword);
        Long tongKhachHang = nguoiDungService.getTongKhachHang();

        model.addAttribute("listKhachHang", listKhachHang);
        model.addAttribute("tongKhachHang", tongKhachHang);
        model.addAttribute("keyword", keyword);
        return "admin/khachhang/index";
    }

    /**
     * Trang chi tiết khách hàng + lịch sử đơn hàng
     */
    @GetMapping("/chitiet/{id}")
    public String chiTiet(@PathVariable Integer id, HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        NguoiDung khachHang = nguoiDungService.getKhachHangById(id);
        if (khachHang == null || "ADMIN".equals(khachHang.getVaiTro())) {
            return "redirect:/admin/khachhang";
        }

        List<DonHang> lichSuDonHang = donHangService.getDonHangByKhachHang(id);

        model.addAttribute("khachHang", khachHang);
        model.addAttribute("lichSuDonHang", lichSuDonHang);
        return "admin/khachhang/chitiet";
    }

    /**
     * Cập nhật thông tin khách hàng (AJAX)
     */
    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody Map<String, String> payload,
                                    HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");

        String hoTen = payload.get("hoTen");
        String soDienThoai = payload.get("soDienThoai");
        String diaChi = payload.get("diaChi");

        if (hoTen == null || hoTen.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Họ tên không được để trống");
        }

        boolean success = nguoiDungService.updateKhachHang(id, hoTen, soDienThoai, diaChi);
        return success ? ResponseEntity.ok("success") : ResponseEntity.badRequest().body("Không tìm thấy khách hàng");
    }

    /**
     * Khóa / Mở khóa tài khoản (AJAX)
     */
    @PostMapping("/toggle-status/{id}")
    @ResponseBody
    public ResponseEntity<?> toggleStatus(@PathVariable Integer id, HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");

        boolean success = nguoiDungService.toggleTrangThai(id);
        if (success) {
            NguoiDung nd = nguoiDungService.getKhachHangById(id);
            return ResponseEntity.ok(Map.of("trangThai", nd.getTrangThai()));
        }
        return ResponseEntity.badRequest().body("Không tìm thấy khách hàng");
    }
}
