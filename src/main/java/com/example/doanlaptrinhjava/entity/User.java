package com.example.doanlaptrinhjava.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "NguoiDung")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdNguoiDung")
    private Integer idNguoiDung;

    @Column(name = "HoTen")
    private String hoTen;

    @Column(name = "Email")
    private String email;

    @Column(name = "DienThoai")
    private String dienThoai;

    // GET SET

    public Integer getIdNguoiDung() {
        return idNguoiDung;
    }

    public void setIdNguoiDung(Integer idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }
}