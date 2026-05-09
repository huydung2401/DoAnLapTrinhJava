package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietDonHang_Topping")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ChiTietDonHangTopping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    // =========================

    @ManyToOne
    @JoinColumn(name = "IdChiTietDonHang")
    private ChiTietDonHang chiTietDonHang;

    // =========================

    @ManyToOne
    @JoinColumn(name = "IdTopping")
    private Topping topping;
}