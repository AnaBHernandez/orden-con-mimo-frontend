package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SuppressWarnings("all")
public class MetodoMimoController {
    
    @GetMapping("/sobre-mimo")
    public String mostrarMetodoMimo(Model model) {
        model.addAttribute("title", "Sobre el Método MIMO");
        return "sobre-mimo";
    }
    
    @GetMapping("/categorias-mimo")
    public String mostrarCategoriasMimo(Model model) {
        model.addAttribute("title", "Categorías del Método MIMO");
        return "categorias-mimo";
    }
    
    @GetMapping("/beneficios-mimo")
    public String mostrarBeneficiosMimo(Model model) {
        model.addAttribute("title", "Beneficios del Método MIMO");
        return "beneficios-mimo";
    }
}