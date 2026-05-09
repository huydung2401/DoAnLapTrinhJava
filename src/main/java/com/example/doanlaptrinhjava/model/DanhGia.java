package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

@Entity
@Table(name = "DanhGia")
public class DanhGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDanhGia;

    @ManyToOne
    @JoinColumn(name = "IdNguoiDung")
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "IdSanPham")
    private SanPham sanPham;

    private Integer soSao;

    private String noiDung;

    private String hinhAnh;

    private String phanHoiAdmin;

    private Boolean daDuyet;

    private LocalDateTime ngayDanhGia;
}