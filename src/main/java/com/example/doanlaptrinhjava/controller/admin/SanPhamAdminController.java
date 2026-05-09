package com.example.doanlaptrinhjava.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SanPhamAdminController {

    @GetMapping("/admin/sanpham")
    public String index() {
        return "admin/sanpham/index";
    }
}