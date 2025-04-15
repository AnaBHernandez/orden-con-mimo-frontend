package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        } catch (Exception e) {
            // En caso de error, mostramos una lista vacía y un mensaje
            model.addAttribute("tareas", Collections.emptyList());
            model.addAttribute("errorMessage", "No se pudieron cargar las tareas: " + e.getMessage());
        }

        return "tarea/list"; // Nombre de la plantilla Thymeleaf
    }

    /**
     * Muestra el formulario para editar una tarea
     */
    @GetMapping("/{id}/editar")
    public String editarTareaForm(@PathVariable Long id, Model model) {
        try {
            Tarea tarea = tareaApiService.obtenerTareaPorId(id);
            if (tarea == null) {
                return "redirect:/tareas?error=Tarea no encontrada";
            }
            model.addAttribute("tarea", tarea);
            return "tarea/editar"; // Plantilla para editar tareas
        } catch (Exception e) {
            return "redirect:/tareas?error=" + e.getMessage();
        }
    }

    /**
     * Elimina una tarea
     */
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
}