package com.example.doanlaptrinhjava.entity;

import com.example.doanlaptrinhjava.model.NguoiDung;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "YeuCauTuVan")
public class TuVan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTuVan")
    private Integer idTuVan;

    // =========================
    // USER
    // =========================

    @ManyToOne
    @JoinColumn(name = "IdNguoiDung")
    private NguoiDung user;

    // =========================
    // NỘI DUNG
    // =========================

    @Column(name = "NoiDung",
            columnDefinition = "TEXT")
    private String noiDung;

    // =========================
    // PHẢN HỒI ADMIN
    // =========================

    @Column(name = "PhanHoiAdmin",
            columnDefinition = "TEXT")
    private String phanHoiAdmin;

    // =========================
    // TRẠNG THÁI
    // =========================

    @Column(name = "TrangThai")
    private String trangThai;

    // =========================
    // NGÀY GỬI
    // =========================

    @Column(name = "NgayGui")
    private LocalDateTime ngayGui;

    // =========================
    // GETTER SETTER
    // =========================

    public Integer getIdTuVan() {
        return idTuVan;
    }

    public void setIdTuVan(Integer idTuVan) {
        this.idTuVan = idTuVan;
    }

    public NguoiDung getUser() {
        return user;
    }

    public void setUser(NguoiDung user) {
        this.user = user;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getPhanHoiAdmin() {
        return phanHoiAdmin;
    }

    public void setPhanHoiAdmin(String phanHoiAdmin) {
        this.phanHoiAdmin = phanHoiAdmin;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LocalDateTime getNgayGui() {
        return ngayGui;
    }

    public void setNgayGui(LocalDateTime ngayGui) {
        this.ngayGui = ngayGui;
    }
}