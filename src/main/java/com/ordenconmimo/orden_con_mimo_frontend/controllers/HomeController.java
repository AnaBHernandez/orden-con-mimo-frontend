package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@SuppressWarnings("all")
public class HomeController {
    
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Reino MIMO - Inicio");
        return "index";  // Cambiado de "home" a "index"
    }
    
    @GetMapping("/bienvenida")
    public String irAPagina(Model model) {
        model.addAttribute("title", "Bienvenida al Reino MIMO");
        return "bienvenida";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Dashboard MIMO");
        return "dashboard";
    }
    
    @GetMapping("/inicio")
    public String inicio() {
        return "redirect:/";
    }
    
    @GetMapping("/salir")
    public String salir() {
        // Redirigir a la página principal en lugar de cerrar sesión
        return "redirect:/";
    }
}