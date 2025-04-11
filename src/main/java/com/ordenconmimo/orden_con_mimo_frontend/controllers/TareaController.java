package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("mensaje", "Lista de Tareas");
        return "tareas/lista";  // Cambio aquí: usamos "tareas/lista"
    }
}