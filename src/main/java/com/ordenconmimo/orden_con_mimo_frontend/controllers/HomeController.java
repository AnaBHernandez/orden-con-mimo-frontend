package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pagina", "inicio");
        return "index";
    }
    
    @GetMapping("/tareas")
    public String tareas(Model model) {
        model.addAttribute("pagina", "tareas");
        return "tarea/index";
    }
    
    @GetMapping("/espacios")
    public String espacios(Model model) {
        model.addAttribute("pagina", "espacios");
        return "espacio/index";
    }
}