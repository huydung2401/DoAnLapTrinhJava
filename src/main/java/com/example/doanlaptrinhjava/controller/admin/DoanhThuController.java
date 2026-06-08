package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/doanhthu")
public class DoanhThuController {

    @Autowired
    private DonHangRepository donHangRepository;

    @GetMapping
    public String index(
            Model model,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate tuNgay,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate denNgay
    ) {

        LocalDateTime from =
                (tuNgay != null)
                        ? tuNgay.atStartOfDay()
                        : LocalDateTime.now().minusDays(30);

        LocalDateTime to =
                (denNgay != null)
                        ? denNgay.atTime(LocalTime.MAX)
                        : LocalDateTime.now();

        Double tongDoanhThu =
                donHangRepository.tinhDoanhThuTheoKhoangThoiGian(
                        from,
                        to
                );

        Long tongDonHang =
                donHangRepository.countDonHangTheoKhoangThoiGian(
                        from,
                        to
                );

        model.addAttribute(
                "tongDoanhThu",
                tongDoanhThu != null ? tongDoanhThu : 0.0
        );

        model.addAttribute(
                "tongDonHang",
                tongDonHang != null ? tongDonHang : 0
        );

        /*
         * =========================
         * DOANH THU THEO NGÀY
         * =========================
         */

        List<Object[]> doanhThuNgay =
                donHangRepository.getDoanhThu7Ngay();

        List<String> labelsNgay =
                new ArrayList<>();

        List<Double> dataNgay =
                new ArrayList<>();

        for (Object[] row : doanhThuNgay) {

            labelsNgay.add(
                    row[0].toString()
            );

            dataNgay.add(
                    row[2] != null
                            ? ((Number) row[2]).doubleValue()
                            : 0.0
            );
        }

        model.addAttribute(
                "labelsNgay",
                labelsNgay
        );

        model.addAttribute(
                "dataNgay",
                dataNgay
        );

        model.addAttribute(
                "listDoanhThuNgay",
                doanhThuNgay
        );

        /*
         * =========================
         * DOANH THU THEO THÁNG
         * =========================
         */

        List<Object[]> doanhThuThang =
                donHangRepository.getDoanhThuTheoThang();

        List<String> labelsThang =
                new ArrayList<>();

        List<Double> dataThang =
                new ArrayList<>();

        for (Object[] row : doanhThuThang) {

            Integer thang =
                    ((Number) row[0]).intValue();

            Integer nam =
                    ((Number) row[1]).intValue();

            labelsThang.add(
                    "T" + thang + "/" + nam
            );

            dataThang.add(
                    row[3] != null
                            ? ((Number) row[3]).doubleValue()
                            : 0.0
            );
        }

        model.addAttribute(
                "labelsThang",
                labelsThang
        );

        model.addAttribute(
                "dataThang",
                dataThang
        );

        model.addAttribute(
                "listDoanhThuThang",
                doanhThuThang
        );

        model.addAttribute(
                "tuNgay",
                from.toLocalDate()
        );

        model.addAttribute(
                "denNgay",
                to.toLocalDate()
        );

        return "admin/doanhthu/index";
    }
}