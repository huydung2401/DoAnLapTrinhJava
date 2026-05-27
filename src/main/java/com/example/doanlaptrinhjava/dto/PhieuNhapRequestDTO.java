package com.example.doanlaptrinhjava.dto;

import lombok.Data;
import java.util.List;

@Data
public class PhieuNhapRequestDTO {
    private String ghiChu;
    private List<ChiTietNhapRequestDTO> chiTiet;
}
