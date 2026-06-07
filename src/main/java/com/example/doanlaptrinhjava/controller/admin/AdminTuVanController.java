package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.entity.TuVan;
import com.example.doanlaptrinhjava.service.TuVanService;
import com.example.doanlaptrinhjava.utils.AdminAuthUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/tuvan")
public class AdminTuVanController {

    @Autowired
    private TuVanService tuVanService;

    /**
     * Danh sách yêu cầu tư vấn, lọc theo trạng thái
     */
    @GetMapping
    public String index(@RequestParam(required = false) String trangThai,
                        HttpSession session, Model model) {
        if (!AdminAuthUtil.isAdmin(session)) return "redirect:/TaiKhoan/DangNhap";

        List<TuVan> listTuVan = tuVanService.getAllTuVan(trangThai);
        Long soChoPhanHoi = tuVanService.getSoTuVanChoPhanHoi();

        model.addAttribute("listTuVan", listTuVan);
        model.addAttribute("soChoPhanHoi", soChoPhanHoi);
        model.addAttribute("trangThaiFilter", trangThai);
        return "admin/tuvan/index";
    }

    /**
     * Admin phản hồi yêu cầu tư vấn (AJAX)
     */
    @PostMapping("/reply/{id}")
    @ResponseBody
    public ResponseEntity<?> reply(@PathVariable Integer id,
                                   @RequestBody Map<String, String> payload,
                                   HttpSession session) {
        if (!AdminAuthUtil.isAdmin(session)) return ResponseEntity.status(401).body("Unauthorized");

        String noiDung = payload.get("noiDung");
        if (noiDung == null || noiDung.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nội dung phản hồi không được để trống");
        }

        boolean success = tuVanService.phanHoiTuVan(id, noiDung);
        return success ? ResponseEntity.ok("success") : ResponseEntity.badRequest().body("Không tìm thấy yêu cầu tư vấn");
    }
}
