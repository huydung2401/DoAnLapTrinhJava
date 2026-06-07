package com.example.doanlaptrinhjava.service;

import com.example.doanlaptrinhjava.entity.TuVan;
import com.example.doanlaptrinhjava.repository.TuVanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TuVanService {

    @Autowired
    private TuVanRepository tuVanRepository;

    /**
     * Lấy danh sách yêu cầu tư vấn (Admin), có thể lọc theo trạng thái
     */
    public List<TuVan> getAllTuVan(String trangThai) {
        if (trangThai != null && !trangThai.trim().isEmpty()) {
            return tuVanRepository.findByTrangThaiOrderByNgayGuiDesc(trangThai);
        }
        return tuVanRepository.findAllByOrderByNgayGuiDesc();
    }

    /**
     * Admin phản hồi yêu cầu tư vấn → tự động đổi trạng thái sang DA_PHAN_HOI
     */
    public boolean phanHoiTuVan(Integer id, String noiDung) {
        Optional<TuVan> opt = tuVanRepository.findById(id);
        if (opt.isPresent() && noiDung != null && !noiDung.trim().isEmpty()) {
            TuVan tv = opt.get();
            tv.setPhanHoiAdmin(noiDung.trim());
            tv.setTrangThai("DA_PHAN_HOI");
            tuVanRepository.save(tv);
            return true;
        }
        return false;
    }

    /**
     * Đếm số yêu cầu tư vấn đang chờ phản hồi
     */
    public Long getSoTuVanChoPhanHoi() {
        return tuVanRepository.countByTrangThai("CHO_PHAN_HOI");
    }
}
