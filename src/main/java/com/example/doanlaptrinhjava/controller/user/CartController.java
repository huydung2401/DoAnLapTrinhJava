package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.*;
import com.example.doanlaptrinhjava.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class CartController {

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private ChiTietGioHangRepository chiTietGioHangRepository;

    @Autowired
    private ChiTietGioHangToppingRepository chiTietGioHangToppingRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private SizeSanPhamRepository sizeSanPhamRepository;

    @Autowired
    private ToppingRepository toppingRepository;

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Integer productId,
                            @RequestParam("Size") Integer sizeId,
                            @RequestParam(value = "toppingIds", required = false) List<Integer> toppingIds,
                            @RequestParam(value = "GhiChu", required = false) String ghiChu,
                            @RequestParam("Quantity") Integer quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        // 1. Get User (Fallback to ID 2 if not logged in)
        NguoiDung user = (NguoiDung) session.getAttribute("User");
        if (user == null) {
            user = nguoiDungRepository.findById(2).orElse(null);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy người dùng!");
                return "redirect:/detail?id=" + productId;
            }
        }

        // 2. Fetch or create GioHang
        Integer currentUserId = user.getIdNguoiDung();
        List<GioHang> gioHangs = gioHangRepository.findAll();
        GioHang gioHang = gioHangs.stream()
                .filter(gh -> gh.getNguoiDung() != null && gh.getNguoiDung().getIdNguoiDung().equals(currentUserId))
                .findFirst()
                .orElse(null);

        if (gioHang == null) {
            gioHang = new GioHang();
            gioHang.setNguoiDung(user);
            gioHang.setNgayTao(new Date());
            gioHang = gioHangRepository.save(gioHang);
        }

        // 3. Fetch related entities
        SanPham sanPham = sanPhamRepository.findById(productId).orElse(null);
        SizeSanPham size = sizeSanPhamRepository.findById(sizeId).orElse(null);

        if (sanPham == null || size == null) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm hoặc Size không hợp lệ!");
            return "redirect:/detail?id=" + productId;
        }

        // 4. Calculate unit price (DonGia)
        double donGia = sanPham.getGia() + size.getGiaThem();

        List<Topping> toppings = null;
        if (toppingIds != null && !toppingIds.isEmpty()) {
            toppings = toppingRepository.findAllById(toppingIds);
            for (Topping t : toppings) {
                donGia += t.getGia();
            }
        }

        // 5. Create ChiTietGioHang
        ChiTietGioHang chiTiet = new ChiTietGioHang();
        chiTiet.setGioHang(gioHang);
        chiTiet.setSanPham(sanPham);
        chiTiet.setSizeSanPham(size);
        chiTiet.setSoLuong(quantity);
        chiTiet.setGhiChu(ghiChu);
        chiTiet.setDonGia(donGia);

        chiTiet = chiTietGioHangRepository.save(chiTiet);

        // 6. Create ChiTietGioHangTopping
        if (toppings != null) {
            for (Topping t : toppings) {
                ChiTietGioHangTopping ctTopping = new ChiTietGioHangTopping();
                ctTopping.setChiTietGioHang(chiTiet);
                ctTopping.setTopping(t);
                chiTietGioHangToppingRepository.save(ctTopping);
            }
        }

        redirectAttributes.addFlashAttribute("success", "Đã thêm vào giỏ hàng thành công!");
        return "redirect:/detail?id=" + productId;
    }
}
