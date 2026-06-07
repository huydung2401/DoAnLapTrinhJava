package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.model.ChiTietDonHang;
import com.example.doanlaptrinhjava.model.DonHang;
import com.example.doanlaptrinhjava.service.DonHangService;
import com.example.doanlaptrinhjava.utils.AdminAuthUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/donhang")
public class AdminDonHangController {

    @Autowired
    private DonHangService donHangService;

    /**
     * Danh sách đơn hàng, lọc theo trạng thái
     */
    @GetMapping
    public String index(@RequestParam(required = false) String trangThai,
                        HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        List<DonHang> listDonHang = donHangService.getAllDonHang(trangThai);
        model.addAttribute("listDonHang", listDonHang);
        model.addAttribute("trangThaiFilter", trangThai);
        return "admin/donhang/index";
    }

    /**
     * Chi tiết một đơn hàng
     */
    @GetMapping("/chitiet/{id}")
    public String chiTiet(@PathVariable Integer id, HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        DonHang donHang = donHangService.getDonHangById(id);
        if (donHang == null) return "redirect:/admin/donhang";

        List<ChiTietDonHang> chiTietList = donHangService.getChiTietDonHang(id);
        model.addAttribute("donHang", donHang);
        model.addAttribute("chiTietList", chiTietList);
        return "admin/donhang/chitiet";
    }

    /**
     * Cập nhật trạng thái đơn hàng tuần tự (AJAX)
     * hanhDong: "NEXT" để chuyển bước tiếp | "HUY" để hủy đơn
     */
    @PostMapping("/update-status/{id}")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@PathVariable Integer id,
                                          @RequestBody Map<String, String> payload,
                                          HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");

        String hanhDong = payload.get("hanhDong");
        if (hanhDong == null || hanhDong.isEmpty()) {
            return ResponseEntity.badRequest().body("Thiếu tham số hanhDong");
        }

        String newStatus = donHangService.updateTrangThai(id, hanhDong);
        if (newStatus != null) {
            return ResponseEntity.ok(Map.of("trangThaiMoi", newStatus));
        }
        return ResponseEntity.badRequest().body("Không thể cập nhật trạng thái đơn hàng này");
    }
}
