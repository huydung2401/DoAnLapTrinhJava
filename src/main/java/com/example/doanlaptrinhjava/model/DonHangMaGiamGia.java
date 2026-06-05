package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DonHang_MaGiamGia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DonHangMaGiamGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne
    @JoinColumn(name = "IdDonHang")
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "IdMaGiamGia")
    private MaGiamGia maGiamGia;
}
