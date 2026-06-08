package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.*;
import com.example.doanlaptrinhjava.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SanPhamController {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private ToppingRepository toppingRepository;

    @Autowired
    private SizeSanPhamRepository sizeSanPhamRepository;
    @Autowired
    private DanhGiaRepository danhGiaRepository;
    // --------------------------------------

    @GetMapping("/menu")
    public String viewMenu(
            Model model,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "new") String sort) {

        List<DanhMuc> danhMucList = danhMucRepository.findAll().stream()
                .filter(DanhMuc::getTrangThai)
                .collect(Collectors.toList());
        List<SanPham> sanPhamList;

        if (categoryId != null && keyword != null && !keyword.trim().isEmpty()) {
            sanPhamList = sanPhamRepository.findByDanhMuc_IdDanhMucAndTenSanPhamContainingIgnoreCaseAndTrangThaiTrue(categoryId, keyword.trim());
        } else if (categoryId != null) {
            sanPhamList = sanPhamRepository.findByDanhMuc_IdDanhMucAndTrangThaiTrue(categoryId);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            sanPhamList = sanPhamRepository.timKiemNangCao(keyword.trim());
        } else {
            sanPhamList = sanPhamRepository.findAll().stream()
                    .filter(SanPham::getTrangThai)
                    .collect(Collectors.toList());
        }
        switch (sort) {

            case "price-asc":
                sanPhamList.sort(
                        (a, b) -> Double.compare(
                                a.getGia(),
                                b.getGia()
                        )
                );
                break;

            case "price-desc":
                sanPhamList.sort(
                        (a, b) -> Double.compare(
                                b.getGia(),
                                a.getGia()
                        )
                );
                break;

            case "name-asc":
                sanPhamList.sort(
                        (a, b) -> a.getTenSanPham()
                                .compareToIgnoreCase(
                                        b.getTenSanPham()
                                )
                );
                break;

            default:
                // Mới nhất
                sanPhamList.sort(
                        (a, b) -> Integer.compare(
                                b.getIdSanPham(),
                                a.getIdSanPham()
                        )
                );
        }

        model.addAttribute("danhMucList", danhMucList);
        model.addAttribute("sanPhamList", sanPhamList);
        model.addAttribute("currentCategoryId", categoryId);
        model.addAttribute("currentKeyword", keyword);
        model.addAttribute("sort", sort);
        return "user/SanPham/menu";
    }

    @GetMapping("/detail")
    public String productDetail(
            @RequestParam(value = "id", required = false) Integer id,
            Model model) {

        if (id == null) {
            return "redirect:/menu";
        }

        SanPham sanPham =
                sanPhamRepository.findById(id).orElse(null);

        if (sanPham == null || !sanPham.getTrangThai()) {
            return "redirect:/menu";
        }

        List<Topping> toppingList =
                toppingRepository.findAll()
                        .stream()
                        .filter(Topping::getTrangThai)
                        .collect(Collectors.toList());

        List<SizeSanPham> sizeList =
                sizeSanPhamRepository.findAll();

        List<SanPham> relatedProducts =
                sanPhamRepository.findAll()
                        .stream()
                        .filter(sp ->
                                sp.getDanhMuc() != null
                                        && sp.getDanhMuc()
                                        .getIdDanhMuc()
                                        .equals(
                                                sanPham.getDanhMuc()
                                                        .getIdDanhMuc()
                                        )
                                        && !sp.getIdSanPham()
                                        .equals(
                                                sanPham.getIdSanPham()
                                        )
                                        && sp.getTrangThai())
                        .limit(4)
                        .collect(Collectors.toList());

        List<DanhGia> danhGiaList =
                danhGiaRepository
                        .findBySanPham_IdSanPhamAndDaDuyetTrueOrderByNgayDanhGiaDesc(id);

        Double avgRating =
                danhGiaRepository.getAvgRating(id);

        Long totalReviews =
                danhGiaRepository.getTotalReviews(id);

        model.addAttribute(
                "danhGiaList",
                danhGiaList != null
                        ? danhGiaList
                        : new ArrayList<>()
        );

        model.addAttribute(
                "avgRating",
                avgRating
        );

        model.addAttribute(
                "totalReviews",
                totalReviews
        );

        model.addAttribute("sanPham", sanPham);
        model.addAttribute("toppingList", toppingList);
        model.addAttribute("sizeList", sizeList);
        model.addAttribute("relatedProducts", relatedProducts);

        return "user/SanPham/detail";
    }
}
