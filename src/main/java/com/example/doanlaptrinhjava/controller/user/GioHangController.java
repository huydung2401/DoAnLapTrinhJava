package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.ChiTietGioHang;
import com.example.doanlaptrinhjava.model.GioHang;
import com.example.doanlaptrinhjava.model.NguoiDung;
import com.example.doanlaptrinhjava.model.SanPham;
import com.example.doanlaptrinhjava.repository.ChiTietGioHangRepository;
import com.example.doanlaptrinhjava.repository.GioHangRepository;
import com.example.doanlaptrinhjava.repository.SanPhamRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/GioHang")

public class GioHangController {

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private ChiTietGioHangRepository chiTietRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    // =========================
    // LẤY USER ĐĂNG NHẬP
    // =========================

    private NguoiDung getUser(HttpSession session){

        return (NguoiDung)
                session.getAttribute("User");
    }

    // =========================
    // XEM GIỎ HÀNG
    // =========================

    @GetMapping
    public String index(Model model,
                        HttpSession session){

        NguoiDung user = getUser(session);

        if(user == null){

            return "redirect:/TaiKhoan/DangNhap";
        }

        GioHang gioHang =
                gioHangRepository
                        .findByNguoiDung_IdNguoiDung(
                                user.getIdNguoiDung()
                        );

        model.addAttribute("gioHang", gioHang);

        return "user/GioHang/index";
    }

    // =========================
    // THÊM VÀO GIỎ HÀNG
    // =========================

    @GetMapping("/Them/{id}")
    public String them(@PathVariable Integer id,
                       HttpSession session){

        NguoiDung user = getUser(session);

        if(user == null){

            return "redirect:/TaiKhoan/DangNhap";
        }

        // LẤY GIỎ HÀNG

        GioHang gioHang =
                gioHangRepository
                        .findByNguoiDung_IdNguoiDung(
                                user.getIdNguoiDung()
                        );

        // CHƯA CÓ GIỎ HÀNG

        if(gioHang == null){

            gioHang = new GioHang();

            gioHang.setNguoiDung(user);

            gioHangRepository.save(gioHang);
        }

        // LẤY SẢN PHẨM

        SanPham sp =
                sanPhamRepository
                        .findById(id)
                        .orElse(null);

        if(sp == null){

            return "redirect:/";
        }

        // KIỂM TRA ĐÃ TỒN TẠI CHƯA

        ChiTietGioHang ctTonTai = null;

        for(ChiTietGioHang item : gioHang.getChiTietGioHangs()){

            if(item.getSanPham()
                    .getIdSanPham()
                    .equals(id)){

                ctTonTai = item;

                break;
            }
        }

        // NẾU ĐÃ CÓ -> TĂNG SỐ LƯỢNG

        if(ctTonTai != null){

            ctTonTai.setSoLuong(
                    ctTonTai.getSoLuong() + 1
            );

            chiTietRepository.save(ctTonTai);
        }

        // NẾU CHƯA CÓ -> THÊM MỚI

        else{

            ChiTietGioHang ct =
                    new ChiTietGioHang();

            ct.setGioHang(gioHang);

            ct.setSanPham(sp);

            ct.setSoLuong(1);

            ct.setDonGia(sp.getGia());

            chiTietRepository.save(ct);
        }

        return "redirect:/GioHang";
    }

    // =========================
    // TĂNG SỐ LƯỢNG
    // =========================

    @GetMapping("/Tang/{id}")
    public String tang(@PathVariable Integer id){

        ChiTietGioHang ct =
                chiTietRepository
                        .findById(id)
                        .orElse(null);

        if(ct != null){

            ct.setSoLuong(
                    ct.getSoLuong() + 1
            );

            chiTietRepository.save(ct);
        }

        return "redirect:/GioHang";
    }

    // =========================
    // GIẢM SỐ LƯỢNG
    // =========================

    @GetMapping("/Giam/{id}")
    public String giam(@PathVariable Integer id){

        ChiTietGioHang ct =
                chiTietRepository
                        .findById(id)
                        .orElse(null);

        if(ct != null){

            if(ct.getSoLuong() > 1){

                ct.setSoLuong(
                        ct.getSoLuong() - 1
                );

                chiTietRepository.save(ct);
            }
        }

        return "redirect:/GioHang";
    }

    // =========================
    // XÓA SẢN PHẨM
    // =========================

    @GetMapping("/Xoa/{id}")
    public String xoa(@PathVariable Integer id){

        chiTietRepository.deleteById(id);

        return "redirect:/GioHang";
    }
}