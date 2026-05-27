package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.model.DanhMuc;
import com.example.doanlaptrinhjava.model.SanPham;
import com.example.doanlaptrinhjava.repository.DanhMucRepository;
import com.example.doanlaptrinhjava.service.SanPhamService;
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
@RequestMapping("/admin/khuyenmai")
public class AdminKhuyenMaiController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @GetMapping
    public String index(@RequestParam(required = false) Integer status, HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        List<SanPham> listSP = sanPhamService.getSanPhamByKhuyenMai(status);
        List<DanhMuc> listDM = danhMucRepository.findAll();

        model.addAttribute("listSP", listSP);
        model.addAttribute("listDM", listDM);
        model.addAttribute("status", status);
        return "admin/khuyenmai/index";
    }

    @PostMapping("/bulk-apply")
    public String bulkApply(
            @RequestParam(required = false) Integer idDanhMuc,
            @RequestParam Double mucGiam,
            HttpSession session, RedirectAttributes ra) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        sanPhamService.applyBulkKhuyenMai(idDanhMuc, mucGiam);
        ra.addFlashAttribute("message", "Áp dụng khuyến mãi hàng loạt thành công!");
        return "redirect:/admin/khuyenmai";
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Map<String, Double> payload, HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");

        Double giaKhuyenMai = payload.get("giaKhuyenMai");
        if (giaKhuyenMai == null || giaKhuyenMai < 0) {
            return ResponseEntity.badRequest().body("Giá khuyến mãi không hợp lệ.");
        }

        sanPhamService.updateGiaKhuyenMai(id, giaKhuyenMai);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/remove/{id}")
    @ResponseBody
    public ResponseEntity<?> remove(@PathVariable("id") Integer id, HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");

        sanPhamService.removeGiaKhuyenMai(id);
        return ResponseEntity.ok("success");
    }
}
