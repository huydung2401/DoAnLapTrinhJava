package com.example.doanlaptrinhjava.service;

import com.example.doanlaptrinhjava.model.DanhGia;
import com.example.doanlaptrinhjava.repository.DanhGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DanhGiaService {

    @Autowired
    private DanhGiaRepository danhGiaRepository;

    public List<DanhGia> getDanhGiaList(Integer status) {
        if (status != null) {
            if (status == 1) {
                return danhGiaRepository.findByDaDuyetOrderByNgayDanhGiaDesc(true);
            } else if (status == 0) {
                return danhGiaRepository.findByDaDuyetOrderByNgayDanhGiaDesc(false);
            }
        }
        return danhGiaRepository.findAllOrderByNgayDanhGiaDesc();
    }

    public boolean updateTrangThaiDuyet(Integer id, Boolean trangThai) {
        Optional<DanhGia> opt = danhGiaRepository.findById(id);
        if (opt.isPresent()) {
            DanhGia danhGia = opt.get();
            danhGia.setDaDuyet(trangThai);
            danhGiaRepository.save(danhGia);
            return true;
        }
        return false;
    }

    public boolean phanHoiDanhGia(Integer id, String noiDungPhanHoi) {
        Optional<DanhGia> opt = danhGiaRepository.findById(id);
        if (opt.isPresent()) {
            DanhGia danhGia = opt.get();
            danhGia.setPhanHoiAdmin(noiDungPhanHoi);
            danhGiaRepository.save(danhGia);
            return true;
        }
        return false;
    }

    public boolean xoaDanhGia(Integer id) {
        if (danhGiaRepository.existsById(id)) {
            danhGiaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Long getSoDanhGiaChoDuyet() {
        return danhGiaRepository.countDanhGiaChoDuyet();
    }
}
