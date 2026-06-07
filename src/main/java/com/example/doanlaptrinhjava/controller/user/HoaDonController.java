package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.DonHang;
import com.example.doanlaptrinhjava.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HoaDonController {

    @Autowired
    private DonHangRepository donHangRepository;

    @GetMapping("/DonHang/XemHoaDon/{id}")
    public String xemHoaDon(@PathVariable("id") Integer id, Model model) {
        DonHang donHang = donHangRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn hàng với mã: " + id));
        model.addAttribute("donHang", donHang);
        return "user/ThanhToan/HoaDon";
    }
}