package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.model.DanhMuc;
import com.example.doanlaptrinhjava.model.SanPham;
import com.example.doanlaptrinhjava.repository.DanhMucRepository;
import com.example.doanlaptrinhjava.service.SanPhamService;
import com.example.doanlaptrinhjava.utils.AdminAuthUtil;
import com.example.doanlaptrinhjava.utils.FileUploadUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/sanpham")
public class SanPhamAdminController {

    @Autowired
    private SanPhamService sanPhamService;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @GetMapping
    public String index(
            @RequestParam(required = false) String tenSanPham,
            @RequestParam(required = false) Integer danhMuc,
            HttpSession session, Model model) {

        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        List<SanPham> listSP = sanPhamService.searchSanPham(tenSanPham, danhMuc);
        List<DanhMuc> listDM = danhMucRepository.findAll();

        model.addAttribute("listSP", listSP);
        model.addAttribute("listDM", listDM);
        model.addAttribute("tenSanPham", tenSanPham);
        model.addAttribute("danhMucId", danhMuc);

        return "admin/sanpham/index";
    }

    @GetMapping("/create")
    public String create(HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        model.addAttribute("sanPham", new SanPham());
        model.addAttribute("listDM", danhMucRepository.findAll());
        return "admin/sanpham/create";
    }

    @PostMapping("/create")
    public String createPost(
            @ModelAttribute("sanPham") SanPham sanPham,
            @RequestParam("fileImage") MultipartFile multipartFile,
            HttpSession session, RedirectAttributes ra) throws IOException {

        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            sanPham.setHinhAnh("/uploads/sanpham/" + fileName);
            sanPhamService.save(sanPham);
            String uploadDir = "src/main/resources/static/uploads/sanpham";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            // Default image if none uploaded
            sanPham.setHinhAnh("/images/default.jpg");
            sanPhamService.save(sanPham);
        }

        ra.addFlashAttribute("message", "Thêm sản phẩm thành công!");
        return "redirect:/admin/sanpham";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        SanPham sanPham = sanPhamService.getSanPhamById(id);
        if (sanPham == null) return "redirect:/admin/sanpham";

        model.addAttribute("sanPham", sanPham);
        model.addAttribute("listDM", danhMucRepository.findAll());
        return "admin/sanpham/edit";
    }

    @PostMapping("/edit")
    public String editPost(
            @ModelAttribute("sanPham") SanPham sanPham,
            @RequestParam("fileImage") MultipartFile multipartFile,
            HttpSession session, RedirectAttributes ra) throws IOException {

        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        SanPham existingSP = sanPhamService.getSanPhamById(sanPham.getIdSanPham());
        
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            sanPham.setHinhAnh("/uploads/sanpham/" + fileName);
            String uploadDir = "src/main/resources/static/uploads/sanpham";
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            // Keep old image
            sanPham.setHinhAnh(existingSP.getHinhAnh());
        }

        sanPham.setNgayThem(existingSP.getNgayThem());
        sanPhamService.save(sanPham);

        ra.addFlashAttribute("message", "Cập nhật sản phẩm thành công!");
        return "redirect:/admin/sanpham";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, HttpSession session, RedirectAttributes ra) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";
        
        try {
            sanPhamService.delete(id);
            ra.addFlashAttribute("message", "Đã xóa sản phẩm thành công!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Không thể xóa sản phẩm này vì có dữ liệu liên quan (ví dụ: đã nhập kho, ...)");
        }
        return "redirect:/admin/sanpham";
    }
}