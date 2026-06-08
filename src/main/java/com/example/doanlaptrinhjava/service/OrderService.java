package com.example.doanlaptrinhjava.service;

import com.example.doanlaptrinhjava.model.LoaiGiamGia;
import com.example.doanlaptrinhjava.model.*;
import com.example.doanlaptrinhjava.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private MaGiamGiaRepository maGiamGiaRepository;

    @Autowired
    private DonHangRepository donHangRepository;

    // 1. Kiểm tra và tính toán giá trị giảm giá
    public Double tinhTienGiam(String maCode, Double tongDonHang) {
        // Dùng Optional để lấy mã giảm giá an toàn
        Optional<MaGiamGia> optionalMa = maGiamGiaRepository.findByMaCode(maCode);

        // Nếu không tồn tại mã, trả về 0
        if (optionalMa.isEmpty()) {
            return 0.0;
        }

        MaGiamGia ma = optionalMa.get();

        // Kiểm tra tính hợp lệ
        if (!ma.getKichHoat() ||
                ma.getNgayBatDau().isAfter(LocalDateTime.now()) ||
                ma.getNgayKetThuc().isBefore(LocalDateTime.now()) ||
                ma.getDaSuDung() >= ma.getSoLuong() ||
                tongDonHang < ma.getDonToiThieu()) {
            return 0.0;
        }

        // Tính toán dựa trên loại giảm giá
        if (ma.getLoaiGiamGia() == LoaiGiamGia.PHAN_TRAM) {
            return tongDonHang * (ma.getGiaTriGiam() / 100);
        } else {
            return ma.getGiaTriGiam();
        }
    }

    // 2. Xác nhận đơn hàng và cập nhật lượt sử dụng mã
    public void hoanTatDonHang(String maCode) {
        if (maCode != null) {
            // Dùng ifPresent để thực hiện logic nếu mã tồn tại
            maGiamGiaRepository.findByMaCode(maCode).ifPresent(ma -> {
                ma.setDaSuDung(ma.getDaSuDung() + 1);
                maGiamGiaRepository.save(ma);
            });
        }
    }
}