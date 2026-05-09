package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.entity.TuVan;
import com.example.doanlaptrinhjava.model.NguoiDung;
import com.example.doanlaptrinhjava.repository.TuVanRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/TuVan")
public class TuVanController {

    @Autowired
    private TuVanRepository tuVanRepository;

    // =========================
    // LẤY USER ĐĂNG NHẬP
    // =========================

    private NguoiDung getCurrentUser(HttpSession session) {

        Object obj = session.getAttribute("User");

        if (obj instanceof NguoiDung) {

            return (NguoiDung) obj;
        }

        return null;
    }

    // =========================
    // DANH SÁCH TƯ VẤN
    // =========================

    @GetMapping
    public String index(Model model,
                        HttpSession session) {

        NguoiDung user = getCurrentUser(session);

        if (user == null) {

            return "redirect:/TaiKhoan/DangNhap";
        }

        List<TuVan> listPhieu =
                tuVanRepository
                        .findByUser_IdNguoiDungOrderByNgayGuiDesc(
                                user.getIdNguoiDung()
                        );

        model.addAttribute("listPhieu", listPhieu);

        return "user/TuVan/index";
    }

    // =========================
    // FORM CREATE
    // =========================

    @GetMapping("/Create")
    public String createForm(Model model,
                             HttpSession session) {

        NguoiDung user = getCurrentUser(session);

        if (user == null) {

            return "redirect:/TaiKhoan/DangNhap";
        }

        model.addAttribute("tuVan", new TuVan());

        return "user/TuVan/create";
    }

    // =========================
    // CREATE POST
    // =========================

    @PostMapping("/Create")
    public String create(@ModelAttribute TuVan tuVan,
                         HttpSession session) {

        NguoiDung user = getCurrentUser(session);

        if (user == null) {

            return "redirect:/TaiKhoan/DangNhap";
        }

        tuVan.setUser(user);

        tuVan.setNgayGui(LocalDateTime.now());

        tuVan.setTrangThai("CHO_PHAN_HOI");

        tuVanRepository.save(tuVan);

        return "redirect:/TuVan";
    }

    // =========================
    // CHI TIẾT
    // =========================

    @GetMapping("/Details/{id}")
    public String details(@PathVariable Integer id,
                          Model model,
                          HttpSession session) {

        NguoiDung user = getCurrentUser(session);

        if (user == null) {

            return "redirect:/TaiKhoan/DangNhap";
        }

        TuVan phieu =
                tuVanRepository
                        .findById(id)
                        .orElse(null);

        if (phieu == null) {

            return "redirect:/TuVan";
        }

        // KIỂM TRA QUYỀN

        if (phieu.getUser() == null ||
                !phieu.getUser()
                        .getIdNguoiDung()
                        .equals(user.getIdNguoiDung())) {

            return "redirect:/TuVan";
        }

        model.addAttribute("phieu", phieu);

        return "user/TuVan/details";
    }
}