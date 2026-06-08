package com.example.doanlaptrinhjava.service;

import com.example.doanlaptrinhjava.model.SanPham;
import com.example.doanlaptrinhjava.repository.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;

@Service
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    public List<SanPham> getAllSanPham() {
        return sanPhamRepository.findAll(
                Sort.by(
                        Sort.Direction.ASC,
                        "idSanPham"
                )
        );
    }

    public List<SanPham> searchSanPham(String tenSanPham, Integer idDanhMuc) {
        if (tenSanPham != null && !tenSanPham.isEmpty() && idDanhMuc != null) {
            return sanPhamRepository.findByTenSanPhamContainingIgnoreCaseAndDanhMuc_IdDanhMuc(tenSanPham, idDanhMuc);
        } else if (tenSanPham != null && !tenSanPham.isEmpty()) {
            return sanPhamRepository.findByTenSanPhamContainingIgnoreCase(tenSanPham);
        } else if (idDanhMuc != null) {
            return sanPhamRepository.findByDanhMuc_IdDanhMuc(idDanhMuc);
        }
        return sanPhamRepository.findAll(
                Sort.by(
                        Sort.Direction.ASC,
                        "idSanPham"
                )
        );
    }

    public SanPham getSanPhamById(Integer id) {
        Optional<SanPham> opt = sanPhamRepository.findById(id);
        return opt.orElse(null);
    }

    public void save(SanPham sanPham) {
        sanPhamRepository.save(sanPham);
    }

    public void delete(Integer id) {
        sanPhamRepository.deleteById(id);
    }

    public List<SanPham> getSanPhamByKhuyenMai(Integer status) {
        if (status != null) {
            if (status == 1) {
                return sanPhamRepository.findByGiaKhuyenMaiIsNotNull();
            } else if (status == 0) {
                return sanPhamRepository.findByGiaKhuyenMaiIsNull();
            }
        }
        return sanPhamRepository.findAll();
    }

    public void updateGiaKhuyenMai(Integer idSanPham, Double giaKhuyenMai) {
        SanPham sp = getSanPhamById(idSanPham);
        if (sp != null) {
            sp.setGiaKhuyenMai(giaKhuyenMai);
            sanPhamRepository.save(sp);
        }
    }

    public void removeGiaKhuyenMai(Integer idSanPham) {
        SanPham sp = getSanPhamById(idSanPham);
        if (sp != null) {
            sp.setGiaKhuyenMai(null);
            sanPhamRepository.save(sp);
        }
    }

    public void applyBulkKhuyenMai(Integer idDanhMuc, Double phanTramGiam) {
        List<SanPham> list;
        if (idDanhMuc == null) {
            list = sanPhamRepository.findAll();
        } else {
            list = sanPhamRepository.findByDanhMuc_IdDanhMuc(idDanhMuc);
        }

        for (SanPham sp : list) {
            if (sp.getGia() != null && phanTramGiam != null && phanTramGiam > 0 && phanTramGiam <= 100) {
                Double giaKhuyenMai = sp.getGia() - (sp.getGia() * phanTramGiam / 100);
                sp.setGiaKhuyenMai(giaKhuyenMai);
            }
        }
        sanPhamRepository.saveAll(list);
    }

    public Long getSoSanPhamSapHetHang() {
        return sanPhamRepository.countSpSapHetHang();
    }

    public List<SanPham> getTop5BanChay() {
        return sanPhamRepository.findTop5BanChay();
    }
}
