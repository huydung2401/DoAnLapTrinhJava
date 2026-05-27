package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.dto.PhieuNhapRequestDTO;
import com.example.doanlaptrinhjava.model.ChiTietNhapKho;
import com.example.doanlaptrinhjava.model.PhieuNhapKho;
import com.example.doanlaptrinhjava.model.SanPham;
import com.example.doanlaptrinhjava.service.NhapKhoService;
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

@Controller
@RequestMapping("/admin/nhapkho")
public class AdminNhapKhoController {

    @Autowired
    private NhapKhoService nhapKhoService;

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping
    public String index(HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";
        
        List<PhieuNhapKho> dsPhieuNhap = nhapKhoService.getAllPhieuNhap();
        model.addAttribute("dsPhieuNhap", dsPhieuNhap);
        return "admin/nhapkho/index";
    }

    @GetMapping("/create")
    public String create(HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";
        
        List<SanPham> listSP = sanPhamService.getAllSanPham();
        model.addAttribute("listSP", listSP);
        return "admin/nhapkho/create";
    }

    @PostMapping("/api/create")
    @ResponseBody
    public ResponseEntity<?> createApi(@RequestBody PhieuNhapRequestDTO requestDTO, HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        
        try {
            nhapKhoService.createPhieuNhap(requestDTO);
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{id}")
    public String getDetail(@PathVariable("id") Integer id, HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";
        
        List<ChiTietNhapKho> chiTietList = nhapKhoService.getChiTietByPhieuNhapId(id);
        model.addAttribute("chiTietList", chiTietList);
        return "admin/nhapkho/fragments/detail-modal :: detailContent";
    }
}
