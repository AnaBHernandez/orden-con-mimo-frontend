package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    private final TareaApiService tareaApiService;

    public TareaController(TareaApiService tareaApiService) {
        this.tareaApiService = tareaApiService;
    }

    @GetMapping
    public String listarTareas(Model model) {
        try {
            List<Tarea> tareas = tareaApiService.obtenerTareas();
            model.addAttribute("tareas", tareas);
            return "tarea/list";
        } catch (Exception e) {
            model.addAttribute("tareas", Collections.emptyList());
            model.addAttribute("errorMessage", "Error al conectar con el backend: " + e.getMessage());
            return "tarea/list";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarTareaForm(@PathVariable Long id, Model model) {
        try {
            Tarea tarea = tareaApiService.obtenerTareaPorId(id);
            if (tarea == null) {
                return "redirect:/tareas?error=Tarea no encontrada";
            }
            model.addAttribute("tarea", tarea);
            return "tarea/editar";
        } catch (Exception e) {
            return "redirect:/tareas?error=" + e.getMessage();
        }
    }

    @GetMapping("/{id}/eliminar")
    public String eliminarTarea(@PathVariable Long id) {
        try {
            boolean eliminado = tareaApiService.eliminarTarea(id);
            if (eliminado) {
                return "redirect:/tareas?mensaje=Tarea eliminada correctamente";
            } else {
                return "redirect:/tareas?error=No se pudo eliminar la tarea";
            }
        } catch (Exception e) {
            return "redirect:/tareas?error=" + e.getMessage();
        }
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNuevaTarea(Model model) {
        Tarea nuevaTarea = new Tarea();
        model.addAttribute("tarea", nuevaTarea);
        return "tarea/crear";
    }

    
    @PostMapping("/guardar")
public String guardarTarea(@ModelAttribute Tarea tarea, Model model) {
    try {
        System.out.println("Guardando tarea: " + tarea.getTitulo());
        System.out.println("Categoría: " + tarea.getCategoria());
        System.out.println("Descripción: " + tarea.getDescripcion());
        System.out.println("Fecha límite: " + tarea.getFechaLimite());
        
        Tarea nuevaTarea = tareaApiService.guardarTarea(tarea);
        
        if (nuevaTarea != null) {
            return "redirect:/tareas?mensaje=Tarea guardada correctamente";
        } else {
            model.addAttribute("error", "Error al guardar la tarea: no se recibió respuesta del servidor");
            return "tarea/crear";
        }
    } catch (Exception e) {
        System.err.println("Error al guardar tarea: " + e.getMessage());
        e.printStackTrace();
        model.addAttribute("error", "Error al guardar la tarea: " + e.getMessage());
        return "tarea/crear";
    }
}
        
    @PostMapping("/{id}/actualizar")
    public String actualizarTarea(@PathVariable Long id, @ModelAttribute Tarea tarea, Model model) {
        try {
            tarea.setId(id);
            Tarea tareaActualizada = tareaApiService.actualizarTarea(tarea);
            
            if (tareaActualizada != null) {
                return "redirect:/tareas?mensaje=Tarea actualizada correctamente";
            } else {
                model.addAttribute("error", "Error al actualizar la tarea");
                model.addAttribute("tarea", tarea);
                return "tarea/editar";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar la tarea: " + e.getMessage());
            model.addAttribute("tarea", tarea);
            return "tarea/editar";
        }
    }
}