package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.*;
import com.example.doanlaptrinhjava.repository.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.doanlaptrinhjava.model.SizeSanPham;
import com.example.doanlaptrinhjava.model.Topping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

import jakarta.transaction.Transactional;
import com.example.doanlaptrinhjava.dto.ThanhToanDTO;
import com.example.doanlaptrinhjava.dto.ChiTietThanhToanDTO;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Map;

@Controller
@RequestMapping("/GioHang")

public class GioHangController {

    @Autowired
    private GioHangRepository gioHangRepository;

    @Autowired
    private ChiTietGioHangRepository chiTietRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private SizeSanPhamRepository sizeRepository;

    @Autowired
    private ToppingRepository toppingRepository;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private ChiTietGioHangToppingRepository chiTietGioHangToppingRepository;

    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;


    private NguoiDung getUser(HttpSession session) {

        return (NguoiDung)
                session.getAttribute("User");
    }


    @GetMapping
    public String index(Model model,
                        HttpSession session) {

        NguoiDung user = getUser(session);

        if (user == null) {
            return "redirect:/TaiKhoan/DangNhap";
        }

        GioHang gioHang =
                gioHangRepository
                        .findByNguoiDung_IdNguoiDung(
                                user.getIdNguoiDung()
                        );

        double tongTien = 0;

        if (gioHang != null) {

            for (ChiTietGioHang item : gioHang.getChiTietGioHangs()) {

                tongTien += item.getThanhTien();
            }
        }

        model.addAttribute("gioHang", gioHang);
        model.addAttribute("tongTien", tongTien);

        return "user/GioHang/index";
    }


    @GetMapping("/Them/{id}")
    public String them(@PathVariable Integer id,
                       HttpSession session) {

        NguoiDung user = getUser(session);

        if (user == null) {

            return "redirect:/TaiKhoan/DangNhap";
        }

        // LẤY GIỎ HÀNG

        GioHang gioHang =
                gioHangRepository
                        .findByNguoiDung_IdNguoiDung(
                                user.getIdNguoiDung()
                        );

        // CHƯA CÓ GIỎ HÀNG

        if (gioHang == null) {

            gioHang = new GioHang();

            gioHang.setNguoiDung(user);

            gioHangRepository.save(gioHang);
        }

        // LẤY SẢN PHẨM

        SanPham sp =
                sanPhamRepository
                        .findById(id)
                        .orElse(null);

        if (sp == null) {

            return "redirect:/";
        }

        // KIỂM TRA ĐÃ TỒN TẠI CHƯA

        ChiTietGioHang ctTonTai = null;

        for (ChiTietGioHang item : gioHang.getChiTietGioHangs()) {

            if (item.getSanPham()
                    .getIdSanPham()
                    .equals(id)) {

                ctTonTai = item;

                break;
            }
        }

        // NẾU ĐÃ CÓ -> TĂNG SỐ LƯỢNG

        if (ctTonTai != null) {

            ctTonTai.setSoLuong(
                    ctTonTai.getSoLuong() + 1
            );

            chiTietRepository.save(ctTonTai);
        }

        // NẾU CHƯA CÓ -> THÊM MỚI

        else {

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

    @PostMapping("/Them")
    public String themMoi(
            @RequestParam Integer productId,
            @RequestParam Integer Size,
            @RequestParam(required = false) java.util.List<Integer> toppingIds,
            @RequestParam(required = false) String GhiChu,
            @RequestParam Integer Quantity,
            HttpSession session) {

        NguoiDung user = getUser(session);

        if (user == null) {
            return "redirect:/TaiKhoan/DangNhap";
        }

        GioHang gioHang =
                gioHangRepository
                        .findByNguoiDung_IdNguoiDung(
                                user.getIdNguoiDung()
                        );

        if (gioHang == null) {

            gioHang = new GioHang();

            gioHang.setNguoiDung(user);

            gioHangRepository.save(gioHang);
        }

        SanPham sp =
                sanPhamRepository
                        .findById(productId)
                        .orElse(null);

        if (sp == null) {
            return "redirect:/";
        }

        SizeSanPham size =
                sizeRepository
                        .findById(Size)
                        .orElse(null);

        double gia = sp.getGia();

        if (size != null) {
            gia += size.getGiaThem();
        }

        ChiTietGioHang ct =
                new ChiTietGioHang();

        ct.setGioHang(gioHang);

        ct.setSanPham(sp);

        ct.setSizeSanPham(size);

        ct.setSoLuong(Quantity);

        ct.setGhiChu(GhiChu);

        ct.setDonGia(gia);

        if (toppingIds != null) {

            for (Integer toppingId : toppingIds) {

                Topping tp =
                        toppingRepository
                                .findById(toppingId)
                                .orElse(null);

                if (tp != null) {

                    ChiTietGioHangTopping ctTop =
                            new ChiTietGioHangTopping();

                    ctTop.setChiTietGioHang(ct);

                    ctTop.setTopping(tp);

                    ct.getToppings().add(ctTop);
                }
            }
        }

        chiTietRepository.save(ct);

        return "redirect:/GioHang";
    }

    // =========================
    // TĂNG SỐ LƯỢNG (ĐỒNG BỘ JSON)
    // =========================
    @GetMapping("/Tang/{id}")
    @ResponseBody
    public java.util.Map<String, Object> tang(@PathVariable Integer id, HttpSession session) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();

        ChiTietGioHang ct = chiTietRepository.findById(id).orElse(null);
        if (ct != null) {
            // 1. Tăng số lượng trong Database
            ct.setSoLuong(ct.getSoLuong() + 1);
            chiTietRepository.save(ct);

            // 2. Tính toán lại tổng tiền của toàn bộ giỏ hàng sau khi tăng
            GioHang gioHang = ct.getGioHang();
            double tongTienMoi = 0;
            if (gioHang != null) {
                List<ChiTietGioHang> dsChiTiet =
                        chiTietRepository.findByGioHang_IdGioHang(
                                gioHang.getIdGioHang()
                        );

                for (ChiTietGioHang item : dsChiTiet) {
                    if (item.getIdChiTietGioHang().equals(id)) {
                        tongTienMoi += item.getDonGia() * ct.getSoLuong();
                    } else {
                        tongTienMoi += item.getThanhTien();
                    }
                }
            }

            // 3. Đóng gói dữ liệu thật trả về cho Frontend
            response.put("soLuongMoi", ct.getSoLuong());
            response.put("tongTienMoi", tongTienMoi);
        }
        return response;
    }

    // =========================
    // GIẢM SỐ LƯỢNG (ĐỒNG BỘ JSON)
    // =========================
    @GetMapping("/Giam/{id}")
    @ResponseBody
    public java.util.Map<String, Object> giam(@PathVariable Integer id, HttpSession session) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();

        ChiTietGioHang ct = chiTietRepository.findById(id).orElse(null);
        if (ct != null && ct.getSoLuong() > 1) {
            // 1. Giảm số lượng trong Database
            ct.setSoLuong(ct.getSoLuong() - 1);
            chiTietRepository.save(ct);

            // 2. Tính toán lại tổng tiền của toàn bộ giỏ hàng sau khi giảm
            GioHang gioHang = ct.getGioHang();
            double tongTienMoi = 0;
            if (gioHang != null) {
                for (ChiTietGioHang item : gioHang.getChiTietGioHangs()) {
                    if (item.getIdChiTietGioHang().equals(id)) {
                        tongTienMoi += item.getDonGia() * ct.getSoLuong();
                    } else {
                        tongTienMoi += item.getThanhTien();
                    }
                }
            }

            // 3. Đóng gói dữ liệu thật trả về cho Frontend
            response.put("soLuongMoi", ct.getSoLuong());
            response.put("tongTienMoi", tongTienMoi);
        }
        return response;
    }

    @GetMapping("/Xoa/{id}")
    @Transactional
    public String xoa(@PathVariable Integer id) {

        // Xóa topping trước
        chiTietGioHangToppingRepository
                .xoaTheoChiTietGioHang(id);

        // Xóa chi tiết giỏ hàng sau
        chiTietRepository.xoaTheoId(id);

        return "redirect:/GioHang";
    }

    @GetMapping("/Popup")
    public String popup(Model model,
                        HttpSession session) {

        NguoiDung user = getUser(session);

        GioHang gioHang = null;
        double tongTien = 0;
        int cartCount = 0;

        if (user != null) {

            gioHang = gioHangRepository
                    .findByNguoiDung_IdNguoiDung(
                            user.getIdNguoiDung()
                    );

            if (gioHang != null) {

                for (ChiTietGioHang item :
                        gioHang.getChiTietGioHangs()) {

                    tongTien += item.getThanhTien();

                    // Đếm tổng số lượng sản phẩm
                    cartCount += item.getSoLuong();
                }
            }
        }
        model.addAttribute("gioHang", gioHang);
        model.addAttribute("tongTien", tongTien);
        model.addAttribute("cartCount", cartCount);

        return "user/GioHang/popup :: popupCart";
    }


    private int tinhTongSoLuong(GioHang gioHang) {

        int tong = 0;

        if (gioHang != null &&
                gioHang.getChiTietGioHangs() != null) {

            for (ChiTietGioHang item :
                    gioHang.getChiTietGioHangs()) {

                tong += item.getSoLuong();
            }
        }

        return tong;
    }

    @PostMapping("/ThemAjax")
    @ResponseBody
    public ResponseEntity<?> themVaoGioHangAjax(
            @RequestParam("productId") Integer productId, // Chuyển về Integer cho đồng bộ với Repository của bạn
            @RequestParam("Quantity") int quantity,
            @RequestParam(value = "Size", required = false) Integer sizeId, // Chuyển về Integer
            @RequestParam(value = "GhiChu", required = false) String ghiChu,
            @RequestParam(value = "toppingIds", required = false) java.util.List<Integer> toppingIds, // Sử dụng List<Integer> giống hàm gốc của bạn
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. Kiểm tra trạng thái đăng nhập của người dùng
            NguoiDung user = getUser(session);
            if (user == null) {
                response.put("success", false);
                response.put("message", "Bạn cần đăng nhập để thực hiện chức năng này!");
                return ResponseEntity.status(401).body(response);
            }

            // 2. Lấy hoặc khởi tạo giỏ hàng cho người dùng
            GioHang gioHang = gioHangRepository.findByNguoiDung_IdNguoiDung(user.getIdNguoiDung());
            if (gioHang == null) {
                gioHang = new GioHang();
                gioHang.setNguoiDung(user);
                gioHangRepository.save(gioHang);
            }

            // 3. Kiểm tra sản phẩm có tồn tại không
            SanPham sp = sanPhamRepository.findById(productId).orElse(null);
            if (sp == null) {
                response.put("success", false);
                response.put("message", "Sản phẩm không tồn tại!");
                return ResponseEntity.status(404).body(response);
            }

            // 4. Tính toán giá sản phẩm dựa theo Size lựa chọn
            SizeSanPham size = null;
            double gia = sp.getGia();
            if (sizeId != null) {
                size = sizeRepository.findById(sizeId).orElse(null);
                if (size != null) {
                    gia += size.getGiaThem();
                }
            }

            List<ChiTietGioHang> dsChiTiet =
                    chiTietRepository.findByGioHang_IdGioHang(
                            gioHang.getIdGioHang()
                    );

            ChiTietGioHang ctTonTai = null;

            for (ChiTietGioHang item : dsChiTiet) {

                boolean cungSanPham =
                        item.getSanPham().getIdSanPham()
                                .equals(productId);

                boolean cungSize =
                        (item.getSizeSanPham() == null && size == null)
                                ||
                                (item.getSizeSanPham() != null
                                        && size != null
                                        && item.getSizeSanPham()
                                        .getIdSize()
                                        .equals(size.getIdSize()));

                boolean cungGhiChu =
                        Objects.equals(
                                item.getGhiChu(),
                                ghiChu
                        );

                List<Integer> toppingCu =
                        item.getToppings()
                                .stream()
                                .map(t -> t.getTopping().getIdTopping())
                                .sorted()
                                .collect(Collectors.toList());

                List<Integer> toppingMoi =
                        toppingIds == null
                                ? new ArrayList<>()
                                : toppingIds.stream()
                                  .sorted()
                                  .collect(Collectors.toList());

                boolean cungTopping =
                        toppingCu.equals(toppingMoi);

                if (cungSanPham
                        && cungSize
                        && cungGhiChu
                        && cungTopping) {

                    ctTonTai = item;
                    break;
                }
            }

            if (ctTonTai != null) {

                ctTonTai.setSoLuong(
                        ctTonTai.getSoLuong() + quantity
                );

                chiTietRepository.save(ctTonTai);

            } else {

                ChiTietGioHang ct = new ChiTietGioHang();

                ct.setGioHang(gioHang);
                ct.setSanPham(sp);
                ct.setSizeSanPham(size);
                ct.setSoLuong(quantity);
                ct.setGhiChu(ghiChu);
                ct.setDonGia(gia);

                if (toppingIds != null) {

                    for (Integer toppingId : toppingIds) {

                        Topping tp =
                                toppingRepository
                                        .findById(toppingId)
                                        .orElse(null);

                        if (tp != null) {

                            ChiTietGioHangTopping ctTop =
                                    new ChiTietGioHangTopping();

                            ctTop.setChiTietGioHang(ct);
                            ctTop.setTopping(tp);

                            ct.getToppings().add(ctTop);
                        }
                    }
                }

                chiTietRepository.save(ct);
            }

            // 8. Tính toán tổng số lượng vật phẩm (hoặc tổng số dòng mặt hàng) trong giỏ để làm mới badge số lượng
            int totalItemsInCart = 0;
            List<ChiTietGioHang> dsMoi =
                    chiTietRepository.findByGioHang_IdGioHang(
                            gioHang.getIdGioHang()
                    );

            for (ChiTietGioHang item : dsMoi) {
                totalItemsInCart += item.getSoLuong();
            }

            // 9. Trả kết quả JSON thành công về cho client
            response.put("success", true);
            response.put("message", "Đã thêm vào giỏ hàng thành công!");
            response.put("cartCount", totalItemsInCart);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Lỗi xử lý hệ thống: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    @GetMapping("/XoaAjax/{id}")
    @ResponseBody
    @Transactional
    public Map<String, Object> xoaAjax(
            @PathVariable Integer id,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {

            System.out.println("========== XOA SAN PHAM ==========");
            System.out.println("ID NHAN DUOC = " + id);

            ChiTietGioHang ct =
                    chiTietRepository.findById(id).orElse(null);

            System.out.println("TIM THAY CT = " + (ct != null));

            if (ct != null) {

                System.out.println("TEN SP = " +
                        ct.getSanPham().getTenSanPham());

                // XÓA TOPPING TRƯỚC
                chiTietGioHangToppingRepository
                        .xoaTheoChiTietGioHang(id);

                // XÓA CHI TIẾT GIỎ HÀNG
                chiTietRepository.xoaTheoId(id);

                chiTietRepository.flush();

                System.out.println("DA XOA DATABASE");
            }

            // Tính lại giỏ hàng
            NguoiDung user = getUser(session);

            double tongTienMoi = 0;
            int totalItemsInCart = 0;

            if (user != null) {

                GioHang gioHang =
                        gioHangRepository.findByNguoiDung_IdNguoiDung(
                                user.getIdNguoiDung()
                        );

                if (gioHang != null) {

                    List<ChiTietGioHang> dsChiTiet =
                            chiTietRepository.findByGioHang_IdGioHang(
                                    gioHang.getIdGioHang()
                            );

                    for (ChiTietGioHang item : dsChiTiet) {

                        tongTienMoi += item.getThanhTien();
                        totalItemsInCart += item.getSoLuong();
                    }
                }
            }

            response.put("success", true);
            response.put("tongTienMoi", tongTienMoi);
            response.put("cartCount", totalItemsInCart);

        } catch (Exception e) {

            e.printStackTrace();

            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }

    @GetMapping("/ThanhToan")
    public String thanhToan(Model model, HttpSession session) {

        NguoiDung user = getUser(session);
        if (user == null) {
            return "redirect:/TaiKhoan/DangNhap";
        }

        GioHang gioHang = gioHangRepository.findByNguoiDung_IdNguoiDung(user.getIdNguoiDung());

        if (gioHang == null || gioHang.getChiTietGioHangs().isEmpty()) {
            return "redirect:/GioHang";
        }

        ThanhToanDTO dto = new ThanhToanDTO();
        dto.setHoTen(user.getHoTen());
        dto.setEmail(user.getEmail());
        dto.setDienThoai(user.getSoDienThoai());
        dto.setDiaChi(user.getDiaChi());

        double tongTien = 0;

        for (ChiTietGioHang item : gioHang.getChiTietGioHangs()) {
            ChiTietThanhToanDTO sp = new ChiTietThanhToanDTO();

            sp.setIdSanPham(item.getSanPham().getIdSanPham());
            sp.setTenSanPham(item.getSanPham().getTenSanPham());
            sp.setHinhAnh(item.getSanPham().getHinhAnh());
            sp.setGia(item.getDonGia());
            sp.setSoLuong(item.getSoLuong());

            // Nạp thêm Size
            if (item.getSizeSanPham() != null) {
                sp.setTenSize(item.getSizeSanPham().getTenSize());
            }

            // Nạp thêm Topping
            if (item.getToppings() != null) {
                List<String> dsTopping = item.getToppings().stream()
                        // Thay vì gọi t.getTenTopping() trực tiếp (lỗi), hãy gọi qua đối tượng Topping bên trong nó:
                        .map(t -> t.getTopping() != null ? t.getTopping().getTenTopping() : "Topping không xác định")
                        .collect(java.util.stream.Collectors.toList());
                sp.setTenToppings(dsTopping);
            }

            dto.getSanPhamMua().add(sp);
            tongTien += item.getThanhTien();
        }

        dto.setTongTien(tongTien);
        session.setAttribute(
                "TONG_TIEN_THANH_TOAN",
                tongTien
        );
        model.addAttribute("thanhToan", dto);

        List<MaGiamGia> dsVoucher =
                maGiamGiaRepository.findVoucherConHan();

        model.addAttribute("dsVoucher", dsVoucher);
        return "user/ThanhToan/index";
    }

    @PostMapping("/MuaNgayAjax")
    @ResponseBody
    public Map<String, Object> muaNgayAjax(
            @RequestParam Integer productId, // Tên phải khớp với name trong thẻ <input>
            @RequestParam Integer Quantity,
            @RequestParam(required = false) Integer Size,
            @RequestParam(required = false) List<Integer> toppingIds,
            @RequestParam(required = false) String GhiChu,
            HttpSession session) {

        // Đóng gói dữ liệu vào một Map
        Map<String, Object> data = new HashMap<>();
        data.put("productId", productId);
        data.put("quantity", Quantity);
        data.put("sizeId", Size);
        data.put("toppingIds", toppingIds);
        data.put("ghiChu", GhiChu);

        // Lưu vào Session để trang ThanhToan đọc được
        session.setAttribute("MUA_NGAY_INFO", data);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }

    @GetMapping("/ThanhToan/MuaNgay")
    public String thanhToanMuaNgay(Model model, HttpSession session) {
        Map<String, Object> data =
                (Map<String, Object>)
                        session.getAttribute(
                                "MUA_NGAY_INFO"
                        );

        if (data == null)
            return "redirect:/GioHang";

        session.removeAttribute(
                "MUA_NGAY_INFO"
        );

        Integer id = (Integer) data.get("productId");
        SanPham sp = sanPhamRepository.findById(id).orElse(null);
        if (sp == null) return "redirect:/";

        ThanhToanDTO dto = new ThanhToanDTO();
        dto.setSanPhamMua(new ArrayList<>());

        // Lấy thông tin user (giả sử bạn có hàm getUser)
        NguoiDung user = getUser(session);
        if (user != null) {
            dto.setHoTen(user.getHoTen());
            dto.setDienThoai(user.getSoDienThoai());
            dto.setDiaChi(user.getDiaChi());
        }

        ChiTietThanhToanDTO chiTiet = new ChiTietThanhToanDTO();

        chiTiet.setIdSanPham(sp.getIdSanPham());
        chiTiet.setTenSanPham(sp.getTenSanPham());
        chiTiet.setHinhAnh(sp.getHinhAnh());
        double gia = sp.getGia();
        chiTiet.setSoLuong(
                (Integer) data.get("quantity")
        );

        // Gán Size
        if (data.get("sizeId") != null) {
            SizeSanPham size =
                    sizeRepository.findById(
                            (Integer) data.get("sizeId")
                    ).orElse(null);
            if (size != null) {
                chiTiet.setTenSize(
                        size.getTenSize()
                );
                gia += size.getGiaThem();
            }
        }

        // Gán Topping
        if (data.get("toppingIds") != null) {
            List<Integer> tIds =
                    (List<Integer>) data.get("toppingIds");
            List<String> tenToppings =
                    new ArrayList<>();
            for (Integer tId : tIds) {
                Topping tp =
                        toppingRepository
                                .findById(tId)
                                .orElse(null);
                if (tp != null) {
                    tenToppings.add(
                            tp.getTenTopping()
                    );
                    gia += tp.getGia();
                }
            }
            chiTiet.setTenToppings(
                    tenToppings
            );
        }

        chiTiet.setGia(gia);

        dto.getSanPhamMua().add(chiTiet);
        double tongTien =
                chiTiet.getGia()
                        * chiTiet.getSoLuong();
        dto.setTongTien(tongTien);

        session.setAttribute(
                "TONG_TIEN_THANH_TOAN",
                dto.getTongTien()
        );
        model.addAttribute("thanhToan", dto);

        List<MaGiamGia> dsVoucher =
                maGiamGiaRepository.findAll();

        model.addAttribute("dsVoucher", dsVoucher);

        return "user/ThanhToan/index";
    }

    // ==================== ĐẶT HÀNG ====================
    @PostMapping("/DatHang")
    @Transactional
    public String datHang(@ModelAttribute ThanhToanDTO dto,
                          HttpSession session) {
        System.out.println("DTO TongTien = " + dto.getTongTien());

        if (dto == null) {
            return "redirect:/GioHang";
        }

        NguoiDung user = getUser(session);

        DonHang donHang = new DonHang();

        donHang.setHoTenNguoiNhan(dto.getHoTen());
        donHang.setSoDienThoai(dto.getDienThoai());
        donHang.setDiaChiGiaoHang(dto.getDiaChi());
        donHang.setGhiChu(dto.getGhiChu());
        donHang.setTongTien(dto.getTongTien());
        donHang.setPhuongThucThanhToan(dto.getPhuongThucThanhToan());

        if ("Banking".equals(dto.getPhuongThucThanhToan())) {
            donHang.setTrangThai("CHO_THANH_TOAN");
        } else {
            donHang.setTrangThai("CHO_XAC_NHAN");
        }
        donHang.setNgayDat(LocalDateTime.now());

        if (user != null) {
            donHang.setNguoiDung(user);
        }

        DonHang donHangDaLuu =
                donHangRepository.save(donHang);

        if (user != null) {

            GioHang gioHang =
                    gioHangRepository.findByNguoiDung_IdNguoiDung(
                            user.getIdNguoiDung()
                    );

            if (gioHang != null) {

                System.out.println(
                        "Dang xoa gio hang ID = "
                                + gioHang.getIdGioHang()
                );

                chiTietRepository.xoaTheoGioHang(
                        gioHang.getIdGioHang()
                );
            }
        }

        session.removeAttribute("MUA_NGAY_INFO");

        if ("Banking".equals(dto.getPhuongThucThanhToan())) {
            return "redirect:/ThanhToan/QR/" + donHangDaLuu.getIdDonHang();
        }

        return "redirect:/DonHang/XemHoaDon/"
                + donHangDaLuu.getIdDonHang();

    }


    @PostMapping("/KiemTraMaGiamGia")
    @ResponseBody
    public Map<String, Object> kiemTraMaGiamGia(
            @RequestParam String code,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        try {

            MaGiamGia voucher =
                    maGiamGiaRepository.findByMaCode(code);

            if (voucher == null) {

                response.put("success", false);
                response.put("message", "Mã giảm giá không tồn tại");

                return response;
            }

            if (!voucher.getKichHoat()) {

                response.put("success", false);
                response.put("message", "Mã giảm giá đã bị khóa");

                return response;
            }

            if (voucher.getNgayKetThuc()
                    .isBefore(LocalDateTime.now())) {

                response.put("success", false);
                response.put("message", "Mã giảm giá đã hết hạn");

                return response;
            }

            double tongTien = 0;
            double discount = 0;

            Object totalObj =
                    session.getAttribute("TONG_TIEN_THANH_TOAN");

            if (totalObj != null) {
                tongTien = Double.parseDouble(totalObj.toString());
            }

            if (voucher.getLoaiGiamGia()
                    == LoaiGiamGia.TIEN_MAT) {

                discount = voucher.getGiaTriGiam();
            } else if (voucher.getLoaiGiamGia()
                    == LoaiGiamGia.PHAN_TRAM) {

                discount =
                        tongTien
                                * voucher.getGiaTriGiam()
                                / 100.0;
            }

            response.put("success", true);
            response.put("discount", discount);
            response.put("message", "Áp dụng thành công");

        } catch (Exception ex) {

            ex.printStackTrace();

            response.put("success", false);
            response.put("message", ex.getMessage());
        }
        return response;
    }
}