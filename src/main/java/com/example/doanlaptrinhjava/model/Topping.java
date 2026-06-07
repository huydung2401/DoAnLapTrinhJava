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
    private Integer IdTopping;

    private String TenTopping;

    private Double Gia;

    private String HinhAnh;

    private Boolean TrangThai;
}