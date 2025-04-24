package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MetodoMimoController {
    
    @GetMapping("/sobre-mimo")
    public String mostrarMetodoMimo(Model model) {
        model.addAttribute("title", "Sobre el Método MIMO");
        return "sobre-mimo";
    }
}