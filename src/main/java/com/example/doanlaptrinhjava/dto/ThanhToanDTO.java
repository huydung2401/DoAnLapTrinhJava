package com.example.doanlaptrinhjava.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ThanhToanDTO {

    private String hoTen;
    private String dienThoai;
    private String email;
    private String diaChi;
    private String ghiChu;

    private String phuongThucThanhToan;

    private Double tongTien;

    private List<ChiTietThanhToanDTO> sanPhamMua = new ArrayList<>();
}