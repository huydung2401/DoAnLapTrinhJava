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
    private String maCode;

    @Column(length = 100)
    private String tenMaGiamGia;

    @Enumerated(EnumType.STRING)
    private LoaiGiamGia loaiGiamGia; // thêm trường này

    @Column(length = 20)
    private String phamVi;

    private Double giaTriGiam;

    private Double donToiThieu;

    private Integer soLuong;

    private Integer daSuDung = 0;

    private LocalDateTime ngayBatDau;

    private LocalDateTime ngayKetThuc;

    private Boolean kichHoat = true;
}