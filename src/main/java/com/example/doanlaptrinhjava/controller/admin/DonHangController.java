package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.model.DonHang;
import com.example.doanlaptrinhjava.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminDonHangController")
@RequestMapping("/admin/donhang")
public class DonHangController {

    @Autowired
    private DonHangRepository donHangRepository;
    
    @GetMapping
    public String index(
            @RequestParam(required = false) String trangThai,
            Model model) {

        if (trangThai != null && !trangThai.isEmpty()) {
            model.addAttribute(
                    "listDonHang",
                    donHangRepository.findByTrangThai(trangThai)
            );
        } else {
            model.addAttribute(
                    "listDonHang",
                    donHangRepository.findAll()
            );
        }

        model.addAttribute("trangThai", trangThai);

        return "admin/donhang/index";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Integer id,
                               @RequestParam String trangThaiMoi,
                               RedirectAttributes ra) {

        DonHang donHang = donHangRepository.findById(id).orElse(null);

        if (donHang == null) {
            ra.addFlashAttribute("ThongBao", "Không tìm thấy đơn hàng!");
            ra.addFlashAttribute("LoaiThongBao", "alert-danger");
            return "redirect:/admin/donhang";
        }

        String current = donHang.getTrangThai();

        boolean hopLe = false;

        // Chờ thanh toán -> Đã thanh toán
        if ("CHO_THANH_TOAN".equals(current)
                && "DA_THANH_TOAN".equals(trangThaiMoi)) {
            hopLe = true;
        }

        // Đã thanh toán -> Chờ xác nhận
        if ("DA_THANH_TOAN".equals(current)
                && "CHO_XAC_NHAN".equals(trangThaiMoi)) {
            hopLe = true;
        }

        // Chờ xác nhận -> Đã xác nhận hoặc Đã hủy
        if ("CHO_XAC_NHAN".equals(current)
                && ("DA_XAC_NHAN".equals(trangThaiMoi)
                || "DA_HUY".equals(trangThaiMoi))) {
            hopLe = true;
        }

        // Đã xác nhận -> Đang giao hoặc Đã hủy
        if ("DA_XAC_NHAN".equals(current)
                && ("DANG_GIAO".equals(trangThaiMoi)
                || "DA_HUY".equals(trangThaiMoi))) {
            hopLe = true;
        }

        // Đang giao -> Đã giao
        if ("DANG_GIAO".equals(current)
                && "DA_GIAO".equals(trangThaiMoi)) {
            hopLe = true;
        }

        // Cho phép giữ nguyên trạng thái
        if (current.equals(trangThaiMoi)) {
            hopLe = true;
        }

        if (!hopLe) {
            ra.addFlashAttribute(
                    "ThongBao",
                    "Không được phép chuyển từ "
                            + current + " sang "
                            + trangThaiMoi
            );
            ra.addFlashAttribute("LoaiThongBao", "alert-danger");

            return "redirect:/admin/donhang";
        }

        donHang.setTrangThai(trangThaiMoi);
        donHangRepository.save(donHang);

        ra.addFlashAttribute(
                "ThongBao",
                "Cập nhật đơn hàng thành công!"
        );
        ra.addFlashAttribute(
                "LoaiThongBao",
                "alert-success"
        );

        return "redirect:/admin/donhang";
    }
}