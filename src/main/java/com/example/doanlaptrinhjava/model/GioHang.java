package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "GioHang")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "IdGioHang")
    private Integer idGioHang;

    @Temporal(TemporalType.TIMESTAMP)

    @Column(name = "NgayTao")
    private Date ngayTao;

    // =========================
    // NGƯỜI DÙNG
    // =========================

    @ManyToOne

    @JoinColumn(name = "IdNguoiDung")
    private NguoiDung nguoiDung;

    // =========================
    // CHI TIẾT GIỎ HÀNG
    // =========================

    @OneToMany(mappedBy = "gioHang",
            cascade = CascadeType.ALL)

    private List<ChiTietGioHang> chiTietGioHangs;
}