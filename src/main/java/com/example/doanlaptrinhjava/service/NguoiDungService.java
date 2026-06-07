package com.example.doanlaptrinhjava.service;

import com.example.doanlaptrinhjava.model.NguoiDung;
import com.example.doanlaptrinhjava.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NguoiDungService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    /**
     * Lấy danh sách khách hàng (chỉ lấy USER), hỗ trợ tìm kiếm theo tên
     */
    public List<NguoiDung> getKhachHangList(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return nguoiDungRepository.findByHoTenContainingIgnoreCaseAndVaiTro(keyword.trim(), "USER");
        }
        return nguoiDungRepository.findByVaiTroOrderByNgayTaoDesc("USER");
    }

    /**
     * Lấy thông tin 1 khách hàng theo ID
     */
    public NguoiDung getKhachHangById(Integer id) {
        return nguoiDungRepository.findById(id).orElse(null);
    }

    /**
     * Cập nhật thông tin khách hàng (chỉ cho phép sửa thông tin cơ bản)
     */
    public boolean updateKhachHang(Integer id, String hoTen, String soDienThoai, String diaChi) {
        Optional<NguoiDung> opt = nguoiDungRepository.findById(id);
        if (opt.isPresent()) {
            NguoiDung nd = opt.get();
            nd.setHoTen(hoTen);
            nd.setSoDienThoai(soDienThoai);
            nd.setDiaChi(diaChi);
            nguoiDungRepository.save(nd);
            return true;
        }
        return false;
    }

    /**
     * Khóa / Mở khóa tài khoản khách hàng
     */
    public boolean toggleTrangThai(Integer id) {
        Optional<NguoiDung> opt = nguoiDungRepository.findById(id);
        if (opt.isPresent()) {
            NguoiDung nd = opt.get();
            nd.setTrangThai(!Boolean.TRUE.equals(nd.getTrangThai()));
            nguoiDungRepository.save(nd);
            return true;
        }
        return false;
    }

    /**
     * Đếm tổng khách hàng
     */
    public Long getTongKhachHang() {
        return nguoiDungRepository.countByVaiTro("USER");
    }
}
