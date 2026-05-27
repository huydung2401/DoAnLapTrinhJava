package com.example.doanlaptrinhjava.service;

import com.example.doanlaptrinhjava.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangService {

    @Autowired
    private DonHangRepository donHangRepository;

    public Long getSoDonHangMoi() {
        return donHangRepository.countDonHangMoi();
    }

    public Double getDoanhThuHomNay() {
        return donHangRepository.getDoanhThuHomNay();
    }

    public List<Object[]> getDoanhThu7NgayQua() {
        return donHangRepository.getDoanhThu7NgayQua();
    }
}
