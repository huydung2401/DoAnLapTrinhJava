package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.DanhGia;
import com.example.doanlaptrinhjava.model.NguoiDung;
import com.example.doanlaptrinhjava.repository.DanhGiaRepository;
import com.example.doanlaptrinhjava.repository.SanPhamRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class DanhGiaController {

    private final String UPLOAD_DIR =
            "src/main/resources/static/uploads/";

    @Autowired
    private DanhGiaRepository danhGiaRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @PostMapping("/submitReview")
    public String submitReview(
            @RequestParam("idSanPham") Integer idSanPham,
            @RequestParam("SoSao") Integer soSao,
            @RequestParam("NoiDung") String noiDung,
            @RequestParam(value = "HinhAnhUpload", required = false)
            MultipartFile file,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        NguoiDung user =
                (NguoiDung) session.getAttribute("User");

        if (user == null) {
            return "redirect:/TaiKhoan/DangNhap";
        }

        DanhGia dg = new DanhGia();

        dg.setNguoiDung(user);

        dg.setSanPham(
                sanPhamRepository
                        .findById(idSanPham)
                        .orElse(null)
        );

        dg.setSoSao(soSao);

        dg.setNoiDung(noiDung);

        dg.setDaDuyet(false);

        dg.setNgayDanhGia(
                java.time.LocalDateTime.now()
        );

        if (file != null && !file.isEmpty()) {

            try {

                Files.createDirectories(
                        Paths.get(UPLOAD_DIR)
                );

                String fileName =
                        UUID.randomUUID()
                                + "_"
                                + file.getOriginalFilename();

                Path path =
                        Paths.get(
                                UPLOAD_DIR + fileName
                        );

                Files.copy(
                        file.getInputStream(),
                        path
                );

                dg.setHinhAnh(
                        "/uploads/" + fileName
                );

            } catch (IOException e) {

                e.printStackTrace();
            }
        }

        danhGiaRepository.save(dg);

        redirectAttributes.addFlashAttribute(
                "success",
                "Đánh giá của bạn đã được gửi và đang chờ duyệt!"
        );

        return "redirect:/detail?id=" + idSanPham;
    }
}