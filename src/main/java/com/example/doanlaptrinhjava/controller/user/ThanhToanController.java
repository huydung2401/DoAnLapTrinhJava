package com.example.doanlaptrinhjava.controller;

import com.example.doanlaptrinhjava.model.DonHang;
import com.example.doanlaptrinhjava.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Controller
public class ThanhToanController {

    @Autowired
    private DonHangRepository donHangRepository;

    @GetMapping("/ThanhToan/QR/{id}")
    public String trangQR(@PathVariable Integer id, Model model) {
        DonHang dh = donHangRepository.findById(id).orElse(null);
        if (dh == null) return "redirect:/home";

        // Cấu hình thông tin tài khoản ngân hàng của bạn
        String acc = "0967390575";
        String bank = "MB";
        String content = "DH" + dh.getIdDonHang();
        String qrUrl = String.format("https://qr.sepay.vn/img?acc=%s&bank=%s&amount=%s&des=%s",
                acc, bank, dh.getTongTien().intValue(), content);

        model.addAttribute("donHang", dh);
        model.addAttribute("qrUrl", qrUrl);
        return "user/payment/qr";
    }

    // API check trạng thái cho JS ở trang QR
    @GetMapping("/api/check-status/{id}")
    @ResponseBody
    public Map<String, String> checkStatus(@PathVariable Integer id) {
        DonHang dh = donHangRepository.findById(id).orElse(null);
        String status = (dh != null) ? dh.getTrangThai() : "NOT_FOUND";
        return Collections.singletonMap("status", status);
    }
}