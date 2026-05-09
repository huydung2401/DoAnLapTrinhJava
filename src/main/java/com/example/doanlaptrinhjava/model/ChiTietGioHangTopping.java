package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietGioHang_Topping")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ChiTietGioHangTopping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    // =========================

    @ManyToOne
    @JoinColumn(name = "IdChiTietGioHang")
    private ChiTietGioHang chiTietGioHang;

    // =========================

    @ManyToOne
    @JoinColumn(name = "IdTopping")
    private Topping topping;
}