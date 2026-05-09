package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "BienTheSanPham")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class BienTheSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdBienThe;

    private Integer SoLuongTon;

    // =========================
    // SAN PHAM
    // =========================

    @ManyToOne
    @JoinColumn(name = "IdSanPham")
    private SanPham sanPham;

    // =========================
    // SIZE
    // =========================

    @ManyToOne
    @JoinColumn(name = "IdSize")
    private SizeSanPham sizeSanPham;
}