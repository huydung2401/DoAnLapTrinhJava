package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.SanPham;
import com.example.doanlaptrinhjava.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @GetMapping("/")
    public String home(Model model) {
        return getHomeView(model);
    }

    @GetMapping("/home")
    public String homePage(Model model){
        return getHomeView(model);
    }

    private String getHomeView(Model model) {

        List<SanPham> sanPhamMoiList = sanPhamRepository.findAll().stream()
                .filter(SanPham::getTrangThai)
                .filter(SanPham::getLaSanPhamMoi)
                .limit(4)
                .collect(Collectors.toList());

        List<SanPham> sanPhamBanChayList = sanPhamRepository.findAll().stream()
                .filter(SanPham::getTrangThai)
                .sorted((a, b) -> Integer.compare(b.getDaBan(), a.getDaBan()))
                .limit(4)
                .collect(Collectors.toList());

        List<SanPham> sanPhamKhuyenMaiList = sanPhamRepository.findAll().stream()
                .filter(SanPham::getTrangThai)
                .filter(sp -> sp.getGiaKhuyenMai() != null)
                .sorted((a, b) -> a.getGiaKhuyenMai().compareTo(b.getGiaKhuyenMai()))
                .limit(4)
                .collect(Collectors.toList());

        model.addAttribute("sanPhamMoiList", sanPhamMoiList);
        model.addAttribute("sanPhamBanChayList", sanPhamBanChayList);
        model.addAttribute("sanPhamKhuyenMaiList", sanPhamKhuyenMaiList);

        return "user/home/index";
    }
}