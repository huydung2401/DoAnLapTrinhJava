package com.example.doanlaptrinhjava.controller;

import com.example.doanlaptrinhjava.model.ChiTietGioHang;
import com.example.doanlaptrinhjava.model.GioHang;
import com.example.doanlaptrinhjava.model.NguoiDung;
import com.example.doanlaptrinhjava.repository.GioHangRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.example.doanlaptrinhjava.controller.user")
public class GlobalControllerAdvice {

    @Autowired
    private GioHangRepository gioHangRepository;

    @ModelAttribute
    public void addGlobalAttributes(Model model,
                                    HttpSession session) {

        NguoiDung user =
                (NguoiDung) session.getAttribute("User");

        int cartCount = 0;

        if (user != null) {

            GioHang gioHang =
                    gioHangRepository
                            .findByNguoiDung_IdNguoiDung(
                                    user.getIdNguoiDung()
                            );

            double tongTien = 0;

            if (gioHang != null
                    && gioHang.getChiTietGioHangs() != null) {

                for (ChiTietGioHang ct
                        : gioHang.getChiTietGioHangs()) {

                    tongTien += ct.getThanhTien();

                    // ĐẾM SỐ LƯỢNG TRONG GIỎ
                    cartCount += ct.getSoLuong();
                }
            }

            model.addAttribute("gioHang", gioHang);
            model.addAttribute("tongTien", tongTien);
        }

        // LUÔN THÊM cartCount
        model.addAttribute("cartCount", cartCount);
    }
}