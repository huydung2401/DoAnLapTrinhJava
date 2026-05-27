package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Topping")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Topping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Ép Spring Boot tìm đúng cột 'IdTopping' viết hoa trong MySQL
    @Column(name = "IdTopping")
    private Integer IdTopping;

    @Column(name = "TenTopping")
    private String TenTopping;

    @Column(name = "Gia")
    private Double Gia;

    @Column(name = "HinhAnh")
    private String HinhAnh;

    @Column(name = "TrangThai")
    private Boolean TrangThai;
}