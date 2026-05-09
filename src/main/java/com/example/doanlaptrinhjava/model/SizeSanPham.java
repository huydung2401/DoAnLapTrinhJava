package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SizeSanPham")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SizeSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdSize;

    private String TenSize;

    private Double GiaThem;
}