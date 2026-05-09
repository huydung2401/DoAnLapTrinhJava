package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "SanPham")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdSanPham;

    @Column(nullable = false, length = 200)
    private String TenSanPham;

    @Column(nullable = false)
    private Double Gia;

    private Double GiaKhuyenMai;

    @Column(columnDefinition = "TEXT")
    private String MoTaNgan;

    @Column(columnDefinition = "LONGTEXT")
    private String MoTaChiTiet;

    private String HinhAnh;

    private Integer SoLuongTon = 0;

    private Integer DaBan = 0;

    private Double DanhGia = 5.0;

    private Boolean LaNoiBat = false;

    private Boolean LaBanChay = false;

    private Boolean LaSanPhamMoi = true;

    private Boolean TrangThai = true;

    private LocalDateTime NgayThem = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "IdDanhMuc")
    private DanhMuc danhMuc;
}