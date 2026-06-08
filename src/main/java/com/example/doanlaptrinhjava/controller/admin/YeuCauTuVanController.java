package com.example.doanlaptrinhjava.controller.admin;

import com.example.doanlaptrinhjava.entity.TuVan;
import com.example.doanlaptrinhjava.repository.TuVanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminTuVanController")
@RequestMapping("/admin/tuvan")
public class YeuCauTuVanController {

    @Autowired
    private TuVanRepository tuVanRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("listTuVan", tuVanRepository.findAll());
        return "admin/tuvan/index";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id,
                          Model model) {

        TuVan tuVan =
                tuVanRepository.findById(id)
                        .orElse(null);

        if (tuVan == null) {
            return "redirect:/admin/tuvan";
        }

        model.addAttribute("tuVan", tuVan);

        return "admin/tuvan/details";
    }

    @PostMapping("/reply")
    public String reply(@RequestParam Integer id,
                        @RequestParam String phanHoiAdmin,
                        RedirectAttributes ra) {
        System.out.println("DA VAO HAM REPLY");
        System.out.println("ID = " + id);
        System.out.println("PHAN HOI = " + phanHoiAdmin);


        TuVan tuVan =
                tuVanRepository.findById(id)
                        .orElse(null);

        if (tuVan != null) {

            tuVan.setPhanHoiAdmin(phanHoiAdmin);
            tuVan.setTrangThai("DA_TU_VAN");

            tuVanRepository.save(tuVan);

            ra.addFlashAttribute(
                    "ThongBaoThanhCong",
                    "Đã phản hồi phiếu #" + id
            );
        }

        return "redirect:/admin/tuvan/details/" + id;
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Integer id,
                               @RequestParam String trangThai,
                               RedirectAttributes ra) {

        TuVan tuVan =
                tuVanRepository.findById(id)
                        .orElse(null);

        if (tuVan != null) {

            tuVan.setTrangThai(trangThai);

            tuVanRepository.save(tuVan);

            ra.addFlashAttribute(
                    "ThongBaoThanhCong",
                    "Đã cập nhật trạng thái phiếu #" + id
            );
        }

        return "redirect:/admin/tuvan";
    }
}