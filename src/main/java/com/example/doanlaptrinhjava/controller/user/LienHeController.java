package com.example.doanlaptrinhjava.controller.user;

import com.example.doanlaptrinhjava.model.LienHe;
import com.example.doanlaptrinhjava.repository.LienHeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/LienHe")

public class LienHeController {

    @Autowired
    private LienHeRepository lienHeRepository;

    // HIỂN THỊ FORM

    @GetMapping
    public String index(Model model){

        model.addAttribute("lienHe", new LienHe());

        return "user/LienHe/index";
    }

    // GỬI LIÊN HỆ

    @PostMapping("/Create")
    public String create(@ModelAttribute LienHe lienHe){

        lienHeRepository.save(lienHe);

        return "redirect:/LienHe?success";
    }
}