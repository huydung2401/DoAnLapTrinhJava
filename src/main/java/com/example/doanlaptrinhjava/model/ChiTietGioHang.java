package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietGioHang")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ChiTietGioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdChiTietGioHang;

    private Integer SoLuong;

    private String GhiChu;

    private Double DonGia;

    // =========================

    @ManyToOne
    @JoinColumn(name = "IdGioHang")
    private GioHang gioHang;

    // =========================

    @ManyToOne
    @JoinColumn(name = "IdSanPham")
    private SanPham sanPham;

    // =========================

    @ManyToOne
    @JoinColumn(name = "IdSize")
    private SizeSanPham sizeSanPham;

    public Double getThanhTien() {
        if (this.DonGia == null || this.SoLuong == null) {
            return 0.0;
        }
        return this.DonGia * this.SoLuong;
    }
}