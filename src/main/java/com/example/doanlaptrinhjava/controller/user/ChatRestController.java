package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.service.AiChatService;
import com.example.doanlaptrinhjava.repository.SanPhamRepository;
import com.example.doanlaptrinhjava.model.SanPham;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private AiChatService aiService;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @PostMapping("/send")
    public Map<String, String> getReply(@RequestBody Map<String, String> payload) {
        String userMsg = payload.get("message");

        List<SanPham> danhSachSanPham = sanPhamRepository.findAll();

        String menu = danhSachSanPham.stream()
                .map(sp -> {
                    String gia = String.format("%,.0f", sp.getGia());
                    String khuyenMai = sp.getGiaKhuyenMai() != null
                            ? " KM: " + String.format("%,.0f", sp.getGiaKhuyenMai()) + "đ"
                            : "";
                    return sp.getTenSanPham() + " (" + gia + "đ)" + khuyenMai;
                })
                .collect(Collectors.joining(" | "));

        String reply = aiService.generateReply(userMsg, "Coffee & More", menu);

        return Map.of("reply", reply);
    }
}