package com.example.doanlaptrinhjava.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChiTietNhapKho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietNhapKho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdChiTietNhap;

    @ManyToOne
    @JoinColumn(name = "IdPhieuNhap")
    @JsonIgnore
    private PhieuNhapKho phieuNhapKho;

    @ManyToOne
    @JoinColumn(name = "IdSanPham")
    private SanPham sanPham;

    private Integer SoLuongNhap;

    private Double GiaNhap;
}
