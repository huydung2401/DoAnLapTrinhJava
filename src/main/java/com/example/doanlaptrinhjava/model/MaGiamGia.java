package com.example.doanlaptrinhjava.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MaGiamGia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaGiamGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdMaGiamGia;

    @Column(unique = true, length = 50)
    private String MaCode;

    @Column(length = 100)
    private String TenMaGiamGia;

    @Enumerated(EnumType.STRING)
    private LoaiGiamGia LoaiGiamGia;

    private Double GiaTriGiam;

    private Double DonToiThieu;

    private Integer SoLuong;

    private Integer DaSuDung = 0;

    private LocalDateTime NgayBatDau;

    private LocalDateTime NgayKetThuc;

    private Boolean KichHoat = true;
}
