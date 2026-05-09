package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LienHe")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class LienHe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer idLienHe;

    private String hoTen;

    private String email;

    private String soDienThoai;

    @Column(columnDefinition = "TEXT")
    private String noiDung;
}