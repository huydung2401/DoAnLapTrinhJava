package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "DonHang")
public class DonHang {

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
    private List<ChiTietDonHang> chiTietDonHangList;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDonHang;

    @ManyToOne
    @JoinColumn(name = "IdNguoiDung")
    private NguoiDung nguoiDung;

    private String hoTenNguoiNhan;

    private String soDienThoai;

    private String diaChiGiaoHang;

    private String ghiChu;

    private Double tongTien;

    private Double phiShip;

    private Double tienGiam;

    private String phuongThucThanhToan;

    private String trangThai;

    private LocalDateTime ngayDat;


}