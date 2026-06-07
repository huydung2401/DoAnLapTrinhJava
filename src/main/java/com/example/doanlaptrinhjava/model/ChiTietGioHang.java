package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    // =========================
    // TOPPING

    @OneToMany(
            mappedBy = "chiTietGioHang",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ChiTietGioHangTopping> toppings = new ArrayList<>();

    // =========================

    public Double getThanhTien() {

        double tong = DonGia != null ? DonGia : 0;

        if (toppings != null) {

            for (ChiTietGioHangTopping ctTop : toppings) {

                if (ctTop.getTopping() != null) {

                    tong += ctTop.getTopping().getGia();
                }
            }
        }

        return tong * (SoLuong != null ? SoLuong : 0);
    }
}