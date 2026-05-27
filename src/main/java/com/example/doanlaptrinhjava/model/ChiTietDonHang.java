package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "ChiTietDonHang")
public class ChiTietDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idChiTietDonHang;

    @ManyToOne
    @JoinColumn(name = "IdDonHang")
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "IdSanPham")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "IdSize")
    private SizeSanPham sizeSanPham;

    private Integer soLuong;

    private Double donGia;
}