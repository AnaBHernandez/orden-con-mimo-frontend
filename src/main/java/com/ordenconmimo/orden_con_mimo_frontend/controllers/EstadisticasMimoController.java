package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;

@Controller
public class EstadisticasMimoController {
    
    @Autowired
    private TareaApiService tareaApiService;
    
    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(Model model) {
        try {
            List<Tarea> tareas = tareaApiService.obtenerTareas();
            
            Map<String, Integer> conteos = new HashMap<>();
            conteos.put("MIRATE", 0);
            conteos.put("IMAGINA", 0);
            conteos.put("MUEVETE", 0);
            conteos.put("ORDENA", 0);
            
            if (tareas != null) {
                for (Tarea tarea : tareas) {
                    String categoria = tarea.getCategoria();
                    if (categoria != null) {
                        conteos.put(categoria, conteos.getOrDefault(categoria, 0) + 1);
                    }
                }
            }
            
            model.addAttribute("estadisticas", conteos);
            model.addAttribute("title", "Estadísticas MIMO");
            
            return "estadisticas";
        } catch (Exception e) {
            model.addAttribute("error", "No se pudieron cargar las estadísticas: " + e.getMessage());
            model.addAttribute("estadisticas", new HashMap<String, Integer>() {{
                put("MIRATE", 0);
                put("IMAGINA", 0);
                put("MUEVETE", 0);
                put("ORDENA", 0);
            }});
            model.addAttribute("title", "Estadísticas MIMO - Error");
            
            return "estadisticas";
        }
    }
}