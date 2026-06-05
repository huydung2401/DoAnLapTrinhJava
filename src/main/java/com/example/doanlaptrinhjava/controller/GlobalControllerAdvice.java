package com.example.doanlaptrinhjava.controller;

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
    public void addGlobalAttributes(Model model, HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("User");
        if (user != null) {
            GioHang gioHang = gioHangRepository.findByNguoiDung_IdNguoiDung(user.getIdNguoiDung());
            model.addAttribute("gioHang", gioHang);
        }
    }
}
