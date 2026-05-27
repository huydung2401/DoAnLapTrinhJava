package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PhieuNhapKho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhieuNhapKho {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdPhieuNhap;

    private Integer IdNhaCungCap;

    private Double TongTien;

    @Column(columnDefinition = "TEXT")
    private String GhiChu;

    private LocalDateTime NgayNhap = LocalDateTime.now();
}
