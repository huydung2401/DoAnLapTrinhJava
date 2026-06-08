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
public class OrderController {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;

    @Autowired
    private ChiTietDonHangToppingRepository chiTietDonHangToppingRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private SizeSanPhamRepository sizeSanPhamRepository;

    @Autowired
    private ToppingRepository toppingRepository;

    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;

    @Autowired
    private DonHangMaGiamGiaRepository donHangMaGiamGiaRepository;

    @PostMapping("/checkout/direct")
    public String checkoutDirect(@RequestParam("productId") Integer productId,
                                 @RequestParam("Size") Integer sizeId,
                                 @RequestParam(value = "toppingIds", required = false) List<Integer> toppingIds,
                                 @RequestParam(value = "GhiChu", required = false) String ghiChu,
                                 @RequestParam("Quantity") Integer quantity,
                                 @RequestParam(value = "MaCode", required = false) String maCode,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        // 1. Get User
        NguoiDung user = (NguoiDung) session.getAttribute("User");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thực hiện chức năng này!");
            return "redirect:/TaiKhoan/DangNhap";
        }

        // 2. Fetch related entities
        SanPham sanPham = sanPhamRepository.findById(productId).orElse(null);
        SizeSanPham size = sizeSanPhamRepository.findById(sizeId).orElse(null);

        if (sanPham == null || size == null) {
            redirectAttributes.addFlashAttribute("error", "Sản phẩm hoặc Size không hợp lệ!");
            return "redirect:/detail?id=" + productId;
        }

        // 3. Calculate unit price (DonGia)
        double donGia = sanPham.getGia() + size.getGiaThem();

        List<Topping> toppings = null;
        if (toppingIds != null && !toppingIds.isEmpty()) {
            toppings = toppingRepository.findAllById(toppingIds);
            for (Topping t : toppings) {
                donGia += t.getGia();
            }
        }

        double tongTien = donGia * quantity;
        double tienGiam = 0.0;
        MaGiamGia mgg = null;

        if (maCode != null && !maCode.trim().isEmpty()) {
            // 1. Dùng .orElse(null) để xử lý Optional từ Repository
            mgg = maGiamGiaRepository.findByMaCode(maCode.trim()).orElse(null);

            // 2. Kiểm tra null và các điều kiện logic
            if (mgg != null && mgg.getKichHoat() &&
                    (mgg.getSoLuong() == null || mgg.getDaSuDung() < mgg.getSoLuong())) {

                if (mgg.getDonToiThieu() == null || tongTien >= mgg.getDonToiThieu()) {

                    // 3. Sử dụng .equals() hoặc so sánh trực tiếp Enum đã tách file
                    if (LoaiGiamGia.PHAN_TRAM.equals(mgg.getLoaiGiamGia())) {
                        tienGiam = tongTien * (mgg.getGiaTriGiam() / 100.0);
                    } else if (LoaiGiamGia.TIEN_MAT.equals(mgg.getLoaiGiamGia())) {
                        tienGiam = mgg.getGiaTriGiam();
                    }

                    // 4. Đảm bảo tiền giảm không vượt quá tổng đơn hàng
                    if (tienGiam > tongTien) {
                        tienGiam = tongTien;
                    }
                }
            }
        }

        // 4. Create DonHang
        DonHang donHang = new DonHang();
        donHang.setNguoiDung(user);
        donHang.setHoTenNguoiNhan(user.getHoTen());
        donHang.setSoDienThoai(user.getSoDienThoai());
        donHang.setDiaChiGiaoHang(user.getDiaChi());
        donHang.setGhiChu("ĐẶT HÀNG MUA NGAY. Ghi chú món: " + (ghiChu != null ? ghiChu : ""));
        donHang.setTongTien(tongTien);
        donHang.setPhiShip(15000.0); // Phi ship co ban
        donHang.setTienGiam(tienGiam);
        donHang.setPhuongThucThanhToan("COD");
        donHang.setNgayDat(java.time.LocalDateTime.now());

        donHang = donHangRepository.save(donHang);

        // 5. Create ChiTietDonHang
        ChiTietDonHang chiTiet = new ChiTietDonHang();
        chiTiet.setDonHang(donHang);
        chiTiet.setSanPham(sanPham);
        chiTiet.setSizeSanPham(size);
        chiTiet.setSoLuong(quantity);
        chiTiet.setDonGia(donGia);

        chiTiet = chiTietDonHangRepository.save(chiTiet);

        // 6. Create ChiTietDonHangTopping
        if (toppings != null) {
            for (Topping t : toppings) {
                ChiTietDonHangTopping ctTopping = new ChiTietDonHangTopping();
                ctTopping.setChiTietDonHang(chiTiet);
                ctTopping.setTopping(t);
                chiTietDonHangToppingRepository.save(ctTopping);
            }
        }

        if (mgg != null && tienGiam > 0) {
            DonHangMaGiamGia dhmgg = new DonHangMaGiamGia();
            dhmgg.setDonHang(donHang);
            dhmgg.setMaGiamGia(mgg);
            donHangMaGiamGiaRepository.save(dhmgg);

            mgg.setDaSuDung(mgg.getDaSuDung() + 1);
            maGiamGiaRepository.save(mgg);
        }

        redirectAttributes.addFlashAttribute("success", "Đặt hàng thành công! Mã đơn của bạn là: " + donHang.getIdDonHang());
        return "redirect:/menu";
    }

    @PostMapping("/api/discount/check")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, Object> checkDiscount(@RequestParam("maCode") String maCode,
                                                       @RequestParam("tongTien") Double tongTien) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        if (maCode == null || maCode.trim().isEmpty()) {
            response.put("valid", false);
            response.put("message", "Mã giảm giá trống.");
            return response;
        }

        MaGiamGia mgg = maGiamGiaRepository.findByMaCode(maCode.trim())
                .orElse(null);
        if (mgg == null || !mgg.getKichHoat()) {
            response.put("valid", false);
            response.put("message", "Mã giảm giá không hợp lệ hoặc đã vô hiệu hóa.");
            return response;
        }

        if (mgg.getSoLuong() != null && mgg.getDaSuDung() >= mgg.getSoLuong()) {
            response.put("valid", false);
            response.put("message", "Mã giảm giá đã hết lượt sử dụng.");
            return response;
        }

        if (mgg.getDonToiThieu() != null && tongTien < mgg.getDonToiThieu()) {
            response.put("valid", false);
            response.put("message", "Đơn hàng chưa đạt mức tối thiểu " + mgg.getDonToiThieu() + "đ.");
            return response;
        }

        double tienGiam = 0.0;
        if (mgg.getLoaiGiamGia() == LoaiGiamGia.PHAN_TRAM) {
            tienGiam = tongTien * (mgg.getGiaTriGiam() / 100.0);
        } else if (mgg.getLoaiGiamGia() == LoaiGiamGia.TIEN_MAT) {
            tienGiam = mgg.getGiaTriGiam();
        }

        if (tienGiam > tongTien) {
            tienGiam = tongTien;
        }

        response.put("valid", true);
        response.put("tienGiam", tienGiam);
        response.put("message", "Áp dụng thành công mã " + mgg.getMaCode());
        return response;
    }
}
