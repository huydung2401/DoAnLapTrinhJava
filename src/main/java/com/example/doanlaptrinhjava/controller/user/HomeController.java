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
        List<SanPham> sanPhamNoiBatList = sanPhamRepository.findAll().stream()
                .filter(SanPham::getTrangThai)
                .filter(SanPham::getLaNoiBat)
                .limit(8)
                .collect(Collectors.toList());
        model.addAttribute("sanPhamNoiBatList", sanPhamNoiBatList);
        return "user/home/index";
    }
}