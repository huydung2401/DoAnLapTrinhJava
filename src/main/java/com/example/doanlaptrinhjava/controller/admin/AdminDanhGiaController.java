package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.model.DanhGia;
import com.example.doanlaptrinhjava.service.DanhGiaService;
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
@RequestMapping("/admin/danhgia")
public class AdminDanhGiaController {

    @Autowired
    private DanhGiaService danhGiaService;

    @GetMapping
    public String index(@RequestParam(required = false) Integer status, HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        List<DanhGia> listDanhGia = danhGiaService.getDanhGiaList(status);
        model.addAttribute("listDanhGia", listDanhGia);
        model.addAttribute("status", status);
        return "admin/danhgia/index";
    }

    @PostMapping("/update-status/{id}")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@PathVariable("id") Integer id, @RequestBody Map<String, Boolean> payload, HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");

        Boolean trangThai = payload.get("trangThai");
        if (trangThai == null) {
            return ResponseEntity.badRequest().body("Thiếu tham số trangThai");
        }

        boolean success = danhGiaService.updateTrangThaiDuyet(id, trangThai);
        if (success) {
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.badRequest().body("Không tìm thấy đánh giá");
    }

    @PostMapping("/reply/{id}")
    @ResponseBody
    public ResponseEntity<?> reply(@PathVariable("id") Integer id, @RequestBody Map<String, String> payload, HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");

        String noiDungPhanHoi = payload.get("noiDungPhanHoi");
        if (noiDungPhanHoi == null || noiDungPhanHoi.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nội dung phản hồi không được để trống");
        }

        boolean success = danhGiaService.phanHoiDanhGia(id, noiDungPhanHoi);
        if (success) {
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.badRequest().body("Không tìm thấy đánh giá");
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<?> delete(@PathVariable("id") Integer id, HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");

        boolean success = danhGiaService.xoaDanhGia(id);
        if (success) {
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.badRequest().body("Không tìm thấy đánh giá");
    }
}
