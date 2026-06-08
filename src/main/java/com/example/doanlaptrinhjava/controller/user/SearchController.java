package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.SanPham;
import com.example.doanlaptrinhjava.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @GetMapping
    public List<SanPham> search(
            @RequestParam String keyword
    ) {

        if (keyword == null || keyword.trim().length() < 1) {
            return List.of();
        }

        return sanPhamRepository
                .findByTenSanPhamContainingIgnoreCaseAndTrangThaiTrue(
                        keyword.trim()
                );
    }
}