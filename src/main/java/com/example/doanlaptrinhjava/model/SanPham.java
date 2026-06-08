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
    @Column(name = "IdSanPham")
    private Integer idSanPham;

    @Column(name = "TenSanPham", nullable = false, length = 200)
    private String tenSanPham;

    @Column(name = "Gia", nullable = false)
    private Double gia;

    @Column(name = "GiaKhuyenMai")
    private Double giaKhuyenMai;

    @Column(columnDefinition = "TEXT")
    private String moTaNgan;

    @Column(columnDefinition = "LONGTEXT")
    private String moTaChiTiet;

    private String hinhAnh;

    private Integer soLuongTon = 0;

    private Integer daBan = 0;

    private Double danhGia = 5.0;

    private Boolean laNoiBat = false;
    private Boolean laBanChay = false;
    private Boolean laSanPhamMoi = true;

    private Boolean trangThai = true;

    private LocalDateTime ngayThem = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "IdDanhMuc")
    private DanhMuc danhMuc;
}