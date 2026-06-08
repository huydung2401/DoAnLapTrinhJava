package com.example.doanlaptrinhjava.model;

import java.time.LocalDateTime;

public class HoatDongGanDay {
    private String noiDung;
    private String icon;
    private LocalDateTime thoiGian;

    // Constructor nhận 3 tham số để dùng trong Controller
    public HoatDongGanDay(String noiDung, String icon, LocalDateTime thoiGian) {
        this.noiDung = noiDung;
        this.icon = icon;
        this.thoiGian = thoiGian;
    }

    // Các Getter (cần thiết để Thymeleaf hiển thị và để sort thời gian)
    public String getNoiDung() {
        return noiDung;
    }

    public String getIcon() {
        return icon;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    // Các Setter (tùy chọn, nếu bạn cần thay đổi dữ liệu sau khi tạo)
    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }
}