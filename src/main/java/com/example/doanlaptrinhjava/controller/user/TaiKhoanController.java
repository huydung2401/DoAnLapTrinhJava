package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.NguoiDung;
import com.example.doanlaptrinhjava.repository.NguoiDungRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/TaiKhoan")
public class TaiKhoanController {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    // =========================
    // ĐĂNG NHẬP
    // =========================

    @GetMapping("/DangNhap")
    public String dangNhapPage(){

        return "user/TaiKhoan/DangNhap";
    }

    @PostMapping("/DangNhap")
    public String dangNhap(
            @RequestParam String email,
            @RequestParam String matKhau,
            HttpSession session,
            Model model
    ){

        NguoiDung user =
                nguoiDungRepository
                        .findByEmailAndMatKhau(
                                email,
                                matKhau
                        );

        if(user != null){

            session.setAttribute("User", user);

            session.setAttribute(
                    "UserName",
                    user.getHoTen()
            );

            return "redirect:/home";
        }

        model.addAttribute(
                "error",
                "Sai email hoặc mật khẩu"
        );

        return "user/TaiKhoan/DangNhap";
    }

    // =========================
    // ĐĂNG KÝ
    // =========================

    @GetMapping("/DangKy")
    public String dangKyPage(){

        return "user/TaiKhoan/DangKy";
    }

    @PostMapping("/DangKy")
    public String dangKy(
            NguoiDung nguoiDung,
            Model model
    ){

        // CHECK EMAIL
        if(
                nguoiDungRepository
                        .existsByEmail(
                                nguoiDung.getEmail()
                        )
        ){

            model.addAttribute(
                    "error",
                    "Email đã tồn tại"
            );

            return "user/TaiKhoan/DangKy";
        }

        // SAVE
        nguoiDungRepository.save(
                nguoiDung
        );

        return "redirect:/TaiKhoan/DangNhap";
    }

    // =========================
    // ĐĂNG XUẤT
    // =========================

    @GetMapping("/DangXuat")
    public String dangXuat(
            HttpSession session
    ){

        session.invalidate();

        return "redirect:/home";
    }

    // =========================
// THÔNG TIN CÁ NHÂN
// =========================

    @GetMapping("/ThongTinCaNhan")
    public String thongTinCaNhan(
            HttpSession session,
            Model model
    ){

        NguoiDung user =
                (NguoiDung) session.getAttribute("User");

        if(user == null){

            return "redirect:/TaiKhoan/DangNhap";
        }

        model.addAttribute(
                "user",
                user
        );

        return "user/TaiKhoan/ThongTinCaNhan";
    }

    // =========================
// CẬP NHẬT HỒ SƠ
// =========================

    @PostMapping("/CapNhatHoSo")
    public String capNhatHoSo(
            @ModelAttribute("user") NguoiDung formUser,
            HttpSession session,
            Model model
    ){

        NguoiDung user =
                (NguoiDung) session.getAttribute("User");

        if(user == null){

            return "redirect:/TaiKhoan/DangNhap";
        }

        // UPDATE
        user.setHoTen(
                formUser.getHoTen()
        );

        user.setSoDienThoai(
                formUser.getSoDienThoai()
        );

        user.setDiaChi(
                formUser.getDiaChi()
        );

        // SAVE DB
        nguoiDungRepository.save(user);

        // UPDATE SESSION
        session.setAttribute(
                "User",
                user
        );

        session.setAttribute(
                "UserName",
                user.getHoTen()
        );

        model.addAttribute(
                "success",
                "Cập nhật hồ sơ thành công"
        );

        model.addAttribute(
                "user",
                user
        );

        return "user/TaiKhoan/ThongTinCaNhan";
    }

    @GetMapping("/TuVanCuaToi")
    public String tuVanCuaToi(
            HttpSession session,
            Model model
    ){

        NguoiDung user =
                (NguoiDung) session.getAttribute("User");

        if(user == null){

            return "redirect:/TaiKhoan/DangNhap";
        }

        model.addAttribute(
                "tuVanList",
                new ArrayList<>()
        );

        return "user/TaiKhoan/TuVanCuaToi";
    }



}