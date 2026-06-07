package com.example.doanlaptrinhjava.dto;

import lombok.Data;
import java.util.List;

@Data
public class ChiTietThanhToanDTO {
    private Integer idSanPham;
    private String tenSanPham;
    private String hinhAnh;
    private Double gia; // Giá đơn vị (đã cộng size/topping nếu có)
    private Integer soLuong;

    // Thêm thông tin bổ sung
    private String tenSize;
    private List<String> tenToppings;

    public Double getThanhTien() {
        return gia * soLuong;
    }
}