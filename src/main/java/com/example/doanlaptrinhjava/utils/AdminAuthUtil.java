package com.example.doanlaptrinhjava.utils;

import com.example.doanlaptrinhjava.model.NguoiDung;
import jakarta.servlet.http.HttpSession;

public class AdminAuthUtil {
    
    /**
     * Kiểm tra xem session hiện tại có phải là Admin không
     * @param session HttpSession hiện tại
     * @return true nếu là ADMIN, false nếu ngược lại
     */
    public static boolean isAdmin(HttpSession session) {
        NguoiDung user = (NguoiDung) session.getAttribute("User");
        return user != null && "ADMIN".equalsIgnoreCase(user.getVaiTro());
    }
}
