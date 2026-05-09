package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "NguoiDung")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdNguoiDung")
    private Integer idNguoiDung;

    @Column(name = "HoTen", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "Email", nullable = false, unique = true)
    private String email;

    @Column(name = "MatKhau", nullable = false)
    private String matKhau;

    @Column(name = "SoDienThoai")
    private String soDienThoai;

    @Column(name = "DiaChi", columnDefinition = "TEXT")
    private String diaChi;

    @Column(name = "AnhDaiDien")
    private String anhDaiDien;

    @Column(name = "VaiTro")
    private String vaiTro = "USER";

    @Column(name = "TrangThai")
    private Boolean trangThai = true;

    @Column(name = "NgayTao")
    private LocalDateTime ngayTao = LocalDateTime.now();
}