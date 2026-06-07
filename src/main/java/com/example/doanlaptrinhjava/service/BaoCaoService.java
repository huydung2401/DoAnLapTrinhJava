package com.example.doanlaptrinhjava.service;

import com.example.doanlaptrinhjava.repository.DonHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BaoCaoService {

    @Autowired
    private DonHangRepository donHangRepository;

    private static final DateTimeFormatter SQL_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Doanh thu theo từng tháng trong năm — trả về List 12 phần tử (0 nếu không có dữ liệu)
     */
    public List<Double> getDoanhThuTheoThang(int nam) {
        List<Object[]> raw = donHangRepository.getDoanhThuTheoThang(nam);
        Double[] result = new Double[12];
        Arrays.fill(result, 0.0);
        for (Object[] row : raw) {
            int thang = ((Number) row[0]).intValue();
            double dt = ((Number) row[1]).doubleValue();
            if (thang >= 1 && thang <= 12) {
                result[thang - 1] = dt;
            }
        }
        return Arrays.asList(result);
    }

    /**
     * Doanh thu theo từng năm
     */
    public Map<String, Object> getDoanhThuTheoNam() {
        List<Object[]> raw = donHangRepository.getDoanhThuTheoNam();
        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        for (Object[] row : raw) {
            labels.add(String.valueOf(((Number) row[0]).intValue()));
            data.add(((Number) row[1]).doubleValue());
        }
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        return result;
    }

    /**
     * Doanh thu theo khoảng ngày
     */
    public Map<String, Object> getDoanhThuTheoKhoangNgay(LocalDate tuNgay, LocalDate denNgay) {
        String from = tuNgay.format(SQL_DATE_FORMAT);
        String to = denNgay.format(SQL_DATE_FORMAT);
        List<Object[]> raw = donHangRepository.getDoanhThuTheoKhoangNgay(from, to + " 23:59:59");

        List<String> labels = new ArrayList<>();
        List<Double> data = new ArrayList<>();
        DateTimeFormatter display = DateTimeFormatter.ofPattern("dd/MM");

        for (Object[] row : raw) {
            // row[0] is java.sql.Date
            java.sql.Date sqlDate = (java.sql.Date) row[0];
            LocalDate date = sqlDate.toLocalDate();
            labels.add(date.format(display));
            data.add(((Number) row[1]).doubleValue());
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("labels", labels);
        result.put("data", data);
        return result;
    }

    public Double getTongDoanhThu() {
        Double val = donHangRepository.getTongDoanhThu();
        return val != null ? val : 0.0;
    }

    public Long getTongDonHoanThanh() {
        Long val = donHangRepository.getTongDonHoanThanh();
        return val != null ? val : 0L;
    }
}
