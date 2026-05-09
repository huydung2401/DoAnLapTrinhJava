package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "DanhMuc")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class DanhMuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdDanhMuc;

    @Column(nullable = false, length = 100)
    private String TenDanhMuc;

    @Column(columnDefinition = "TEXT")
    private String MoTa;

    private String HinhAnh;

    private Boolean TrangThai = true;

    @OneToMany(mappedBy = "danhMuc")
    private List<SanPham> danhSachSanPham;
}