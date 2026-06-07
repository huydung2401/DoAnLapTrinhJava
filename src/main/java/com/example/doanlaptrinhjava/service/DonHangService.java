package com.example.doanlaptrinhjava.service;

import com.example.doanlaptrinhjava.model.ChiTietDonHang;
import com.example.doanlaptrinhjava.model.DonHang;
import com.example.doanlaptrinhjava.repository.ChiTietDonHangRepository;
import com.example.doanlaptrinhjava.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonHangService {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;

    // ============================================================
    // Dùng cho Dashboard (giữ nguyên, không thay đổi)
    // ============================================================

    public Long getSoDonHangMoi() {
        return donHangRepository.countDonHangMoi();
    }

    public Double getDoanhThuHomNay() {
        return donHangRepository.getDoanhThuHomNay();
    }

    public List<Object[]> getDoanhThu7NgayQua() {
        return donHangRepository.getDoanhThu7NgayQua();
    }

    // ============================================================
    // Admin: Quản lý Đơn hàng
    // ============================================================

    /**
     * Lấy tất cả đơn hàng, có thể lọc theo trạng thái
     */
    public List<DonHang> getAllDonHang(String trangThai) {
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            return donHangRepository.findByTrangThaiOrderByNgayDatDesc(trangThai);
        }
        return donHangRepository.findAllByOrderByNgayDatDesc();
    }

    /**
     * Lấy chi tiết 1 đơn hàng
     */
    public DonHang getDonHangById(Integer id) {
        return donHangRepository.findById(id).orElse(null);
    }

    /**
     * Lấy chi tiết sản phẩm trong đơn hàng
     */
    public List<ChiTietDonHang> getChiTietDonHang(Integer idDonHang) {
        return chiTietDonHangRepository.findByDonHang_IdDonHang(idDonHang);
    }

    /**
     * Cập nhật trạng thái đơn hàng TUẦN TỰ:
     * CHO_XAC_NHAN → DA_XAC_NHAN → DANG_PHA_CHE → DANG_GIAO → HOAN_THANH
     * Bất kỳ trạng thái nào cũng có thể HUY (DA_HUY)
     * Trả về trạng thái mới nếu thành công, null nếu thất bại
     */
    public String updateTrangThai(Integer id, String hanhDong) {
        Optional<DonHang> opt = donHangRepository.findById(id);
        if (opt.isEmpty()) return null;

        DonHang donHang = opt.get();
        String current = donHang.getTrangThai();
        String next = null;

        switch (hanhDong) {
            case "NEXT":
                // Chuyển sang trạng thái kế tiếp trong luồng
                switch (current) {
                    case "CHO_XAC_NHAN":   next = "DA_XAC_NHAN";  break;
                    case "DA_XAC_NHAN":    next = "DANG_PHA_CHE"; break;
                    case "DANG_PHA_CHE":   next = "DANG_GIAO";    break;
                    case "DANG_GIAO":      next = "HOAN_THANH";   break;
                    default: break;
                }
                break;
            case "HUY":
                if (!"HOAN_THANH".equals(current) && !"DA_HUY".equals(current)) {
                    next = "DA_HUY";
                }
                break;
        }

        if (next != null) {
            donHang.setTrangThai(next);
            donHangRepository.save(donHang);
            return next;
        }
        return null;
    }

    /**
     * Lấy danh sách đơn hàng của 1 khách hàng
     */
    public List<DonHang> getDonHangByKhachHang(Integer idNguoiDung) {
        return donHangRepository.findByNguoiDung_IdNguoiDungOrderByNgayDatDesc(idNguoiDung);
    }

    // ============================================================
    // Admin: Báo cáo Doanh thu
    // ============================================================

    public List<Object[]> getDoanhThuTheoThang(int nam) {
        return donHangRepository.getDoanhThuTheoThang(nam);
    }

    public List<Object[]> getDoanhThuTheoNam() {
        return donHangRepository.getDoanhThuTheoNam();
    }

    public List<Object[]> getDoanhThuTheoKhoangNgay(String tuNgay, String denNgay) {
        return donHangRepository.getDoanhThuTheoKhoangNgay(tuNgay, denNgay);
    }

    public Double getTongDoanhThu() {
        return donHangRepository.getTongDoanhThu();
    }

    public Long getTongDonHoanThanh() {
        return donHangRepository.getTongDonHoanThanh();
    }
}

