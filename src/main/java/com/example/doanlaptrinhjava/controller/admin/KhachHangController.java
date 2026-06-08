package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.model.NguoiDung;
import com.example.doanlaptrinhjava.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/khachhang")
public class KhachHangController {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    // 1. Hiển thị danh sách khách hàng và Lọc
    @GetMapping
    public String index(Model model,
                        @RequestParam(required = false) String keyword,
                        @RequestParam(required = false) Boolean trangThai) {

        List<NguoiDung> listKhachHang;

        if (keyword != null && !keyword.trim().isEmpty()) {

            listKhachHang =
                    nguoiDungRepository
                            .findByHoTenContainingIgnoreCaseOrEmailContainingIgnoreCaseOrSoDienThoaiContaining(
                                    keyword,
                                    keyword,
                                    keyword
                            );

            if (trangThai != null) {
                listKhachHang =
                        listKhachHang.stream()
                                .filter(x -> x.getTrangThai().equals(trangThai))
                                .toList();
            }

        } else if (trangThai != null) {

            listKhachHang =
                    nguoiDungRepository.findByTrangThai(trangThai);

        } else {

            listKhachHang =
                    nguoiDungRepository.findAll();
        }

        model.addAttribute("listKhachHang", listKhachHang);
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThai);

        return "admin/khachhang/index";
    }

    // 2. Khóa tài khoản
    @GetMapping("/khoa/{id}")
    public String khoaTaiKhoan(@PathVariable Integer id, RedirectAttributes ra) {
        NguoiDung user = nguoiDungRepository.findById(id).orElse(null);
        if (user != null) {
            user.setTrangThai(false);
            nguoiDungRepository.save(user);
            ra.addFlashAttribute("ThongBao", "Đã khóa tài khoản " + user.getHoTen());
            ra.addFlashAttribute("LoaiThongBao", "alert-danger");
        }
        return "redirect:/admin/khachhang";
    }

    // 3. Mở khóa tài khoản
    @GetMapping("/mokhoa/{id}")
    public String moKhoaTaiKhoan(@PathVariable Integer id, RedirectAttributes ra) {
        NguoiDung user = nguoiDungRepository.findById(id).orElse(null);
        if (user != null) {
            user.setTrangThai(true);
            nguoiDungRepository.save(user);
            ra.addFlashAttribute("ThongBao", "Đã mở khóa tài khoản " + user.getHoTen());
            ra.addFlashAttribute("LoaiThongBao", "alert-success");
        }
        return "redirect:/admin/khachhang";
    }
}