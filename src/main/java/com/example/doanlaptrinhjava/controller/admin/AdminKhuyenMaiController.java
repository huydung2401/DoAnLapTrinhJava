package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.model.*;
import com.example.doanlaptrinhjava.repository.*;
import com.example.doanlaptrinhjava.service.SanPhamService;
import com.example.doanlaptrinhjava.utils.AdminAuthUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/khuyenmai")
public class AdminKhuyenMaiController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;

    // --- PHẦN KHUYẾN MÃI SẢN PHẨM ---
    @GetMapping
    public String index(@RequestParam(required = false) Integer status,
                        HttpSession session,
                        Model model) {

        if (!AdminAuthUtil.isAdmin(session))
            return "redirect:/TaiKhoan/DangNhap";

        List<SanPham> listSP = sanPhamService.getSanPhamByKhuyenMai(status);
        List<DanhMuc> listDM = danhMucRepository.findAll();
        List<MaGiamGia> listMa = maGiamGiaRepository.findAll();

        model.addAttribute("listSP", listSP);
        model.addAttribute("listDM", listDM);
        model.addAttribute("listMa", listMa); // THÊM DÒNG NÀY
        model.addAttribute("status", status);

        return "admin/khuyenmai/index";
    }

    // --- PHẦN QUẢN LÝ MÃ GIẢM GIÁ (VOUCHER) ---
    @GetMapping("/ma-giam-gia")
    public String listVoucher(HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        model.addAttribute("listMa", maGiamGiaRepository.findAll());
        return "admin/khuyenmai/index";
    }

    @GetMapping("/xoa-ma/{id}")
    public String xoaMa(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes ra) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        maGiamGiaRepository.deleteById(id);
        ra.addFlashAttribute("message", "Đã xóa mã giảm giá.");
        return "redirect:/admin/khuyenmai"; // KHÔNG thêm /index
    }

    // --- CÁC PHƯƠNG THỨC BULK/UPDATE (GIỮ NGUYÊN) ---
    @PostMapping("/bulk-apply")
    public String bulkApply(@RequestParam(required = false) Integer idDanhMuc, @RequestParam Double mucGiam, HttpSession session, RedirectAttributes ra) {
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
        if (giaKhuyenMai == null || giaKhuyenMai < 0) return ResponseEntity.badRequest().body("Giá không hợp lệ.");
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

    @PostMapping("/tao-ma")
    @ResponseBody
    public Map<String, Object> taoMaAjax(@ModelAttribute MaGiamGia maGiamGia, HttpSession session) {
        Map<String, Object> res = new HashMap<>();

        if (!AdminAuthUtil.isAdmin(session)) {
            res.put("success", false);
            res.put("message", "Unauthorized");
            return res;
        }

        // Kiểm tra trùng maCode
        if (maGiamGiaRepository.findByMaCode(maGiamGia.getMaCode()).isPresent()) {
            res.put("success", false);
            res.put("message", "Mã Code đã tồn tại. Vui lòng nhập mã khác.");
            return res;
        }

        maGiamGia = maGiamGiaRepository.save(maGiamGia);

        res.put("success", true);
        res.put("idMaGiamGia", maGiamGia.getIdMaGiamGia());
        res.put("maCode", maGiamGia.getMaCode());
        res.put("giaTriGiam", maGiamGia.getGiaTriGiam());
        res.put("loaiGiamGia", maGiamGia.getLoaiGiamGia());
        res.put("soLuong", maGiamGia.getSoLuong());
        res.put("ngayBatDau", maGiamGia.getNgayBatDau());
        res.put("ngayKetThuc", maGiamGia.getNgayKetThuc());

        return res;
    }
}