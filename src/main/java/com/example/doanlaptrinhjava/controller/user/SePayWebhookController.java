package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.DonHang;
import com.example.doanlaptrinhjava.repository.DonHangRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SePayWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(SePayWebhookController.class);

    @Autowired
    private DonHangRepository donHangRepository;

    @PostMapping("/api/sepay/webhook")
    public String webhook(@RequestBody Map<String, Object> body) {

        logger.info("WEBHOOK DATA = {}", body);

        try {

            if (body.get("content") == null ||
                    body.get("transferAmount") == null) {
                return "Invalid Data";
            }

            String content =
                    body.get("content").toString();

            Long amount =
                    Long.parseLong(
                            body.get("transferAmount")
                                    .toString()
                    );

            // Tìm DHxx trong nội dung chuyển khoản
            java.util.regex.Pattern pattern =
                    java.util.regex.Pattern.compile("DH(\\d+)");

            java.util.regex.Matcher matcher =
                    pattern.matcher(content);

            if (!matcher.find()) {
                logger.warn(
                        "Không tìm thấy mã đơn hàng trong nội dung: {}",
                        content
                );
                return "Order ID Not Found";
            }

            Integer orderId =
                    Integer.parseInt(
                            matcher.group(1)
                    );

            logger.info(
                    "Order ID = {}",
                    orderId
            );

            DonHang dh =
                    donHangRepository
                            .findById(orderId)
                            .orElse(null);

            if (dh == null) {
                logger.warn(
                        "Không tìm thấy đơn hàng {}",
                        orderId
                );
                return "Order Not Found";
            }

            if ("DA_THANH_TOAN".equals(
                    dh.getTrangThai()
            )) {
                return "Already Processed";
            }

            if (dh.getTongTien().longValue()
                    == amount) {

                dh.setTrangThai(
                        "DA_THANH_TOAN"
                );

                donHangRepository.save(dh);

                logger.info(
                        "Đơn hàng {} đã thanh toán thành công.",
                        orderId
                );

                return "OK";
            }

            logger.warn(
                    "Số tiền không khớp. Đơn hàng {}, DB={}, Nhận={}",
                    orderId,
                    dh.getTongTien(),
                    amount
            );

        } catch (Exception e) {

            logger.error(
                    "Lỗi Webhook:",
                    e
            );
        }

        return "Failed";
    }

    @GetMapping("/api/sepay/test/{id}")
    @ResponseBody
    public String test(@PathVariable Integer id) {

        DonHang dh =
                donHangRepository.findById(id)
                        .orElse(null);

        if (dh == null) {
            return "NOT FOUND";
        }

        dh.setTrangThai("DA_THANH_TOAN");

        donHangRepository.save(dh);

        return "OK";
    }
}