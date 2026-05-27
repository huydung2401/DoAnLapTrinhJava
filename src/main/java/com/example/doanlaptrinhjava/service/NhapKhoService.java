package com.example.doanlaptrinhjava.service;

import com.example.doanlaptrinhjava.dto.ChiTietNhapRequestDTO;
import com.example.doanlaptrinhjava.dto.PhieuNhapRequestDTO;
import com.example.doanlaptrinhjava.model.ChiTietNhapKho;
import com.example.doanlaptrinhjava.model.PhieuNhapKho;
import com.example.doanlaptrinhjava.model.SanPham;
import com.example.doanlaptrinhjava.repository.ChiTietNhapKhoRepository;
import com.example.doanlaptrinhjava.repository.PhieuNhapKhoRepository;
import com.example.doanlaptrinhjava.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NhapKhoService {

    @Autowired
    private PhieuNhapKhoRepository phieuNhapKhoRepository;

    @Autowired
    private ChiTietNhapKhoRepository chiTietNhapKhoRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    public List<PhieuNhapKho> getAllPhieuNhap() {
        return phieuNhapKhoRepository.findAllByOrderByNgayNhapDesc();
    }

    public List<ChiTietNhapKho> getChiTietByPhieuNhapId(Integer idPhieuNhap) {
        return chiTietNhapKhoRepository.findByPhieuNhapKho_IdPhieuNhap(idPhieuNhap);
    }

    @Transactional
    public void createPhieuNhap(PhieuNhapRequestDTO requestDTO) {
        // 1. Lưu Phiếu Nhập
        PhieuNhapKho phieuNhap = new PhieuNhapKho();
        phieuNhap.setGhiChu(requestDTO.getGhiChu());
        phieuNhap.setNgayNhap(LocalDateTime.now());
        
        Double tongTien = 0.0;
        
        // 2. Tính tổng tiền & chuẩn bị chi tiết
        for (ChiTietNhapRequestDTO ctDto : requestDTO.getChiTiet()) {
            tongTien += (ctDto.getSoLuong() * ctDto.getGiaNhap());
        }
        phieuNhap.setTongTien(tongTien);
        
        phieuNhapKhoRepository.save(phieuNhap);

        // 3. Lưu Chi Tiết Nhập Kho
        for (ChiTietNhapRequestDTO ctDto : requestDTO.getChiTiet()) {
            Optional<SanPham> spOpt = sanPhamRepository.findById(ctDto.getIdSanPham());
            if (spOpt.isPresent()) {
                ChiTietNhapKho ct = new ChiTietNhapKho();
                ct.setPhieuNhapKho(phieuNhap);
                ct.setSanPham(spOpt.get());
                ct.setSoLuongNhap(ctDto.getSoLuong());
                ct.setGiaNhap(ctDto.getGiaNhap());
                chiTietNhapKhoRepository.save(ct);
                
                // Ghi chú: Trigger TRG_CapNhatTonKho trong Database SQL 
                // sẽ tự động chạy sau khi insert vào bảng ChiTietNhapKho 
                // để cộng thêm số lượng tồn vào bảng SanPham. 
                // Nên không cần gọi lệnh update số lượng bằng code Java.
            }
        }
    }
}
