
package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.DonHang;
import com.example.doanlaptrinhjava.model.NguoiDung;
import com.example.doanlaptrinhjava.repository.DonHangRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller("userDonHangController")
public class DonHangController {

    @Autowired
    private DonHangRepository donHangRepository;

    @GetMapping("/DonHang/CuaToi")
    public String donHangCuaToi(
            Model model,
            HttpSession session
    ) {

        NguoiDung user =
                (NguoiDung) session.getAttribute("User");

        if (user == null) {
            return "redirect:/";
        }

        List<DonHang> danhSach =
                donHangRepository
                        .findByNguoiDung_IdNguoiDungOrderByNgayDatDesc(
                                user.getIdNguoiDung()
                        );

        model.addAttribute(
                "danhSach",
                danhSach
        );

        return "user/donhang/index";
    }
}

