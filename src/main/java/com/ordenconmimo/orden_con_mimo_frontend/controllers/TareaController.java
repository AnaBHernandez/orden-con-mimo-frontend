package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;

import java.time.LocalDate;
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
        Tarea tarea = tareaApiService.obtenerTareaPorId(id);

        if (tarea == null) {
            return "redirect:/tareas?error=Tarea no encontrada";
        }

        if (tarea.getFechaLimite() != null) {
            tarea.actualizarFechaLimiteStr();
            System.out.println("Fecha límite para formulario: " + tarea.getFechaLimiteStr());
        }

        model.addAttribute("tarea", tarea);

        return "tarea/editar";
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
    public String guardarTarea(@ModelAttribute Tarea tarea, Model model, RedirectAttributes redirectAttributes) {
        System.out.println("Recibiendo tarea para guardar: " + tarea);
        System.out.println("Fecha límite recibida: " + tarea.getFechaLimiteStr());

        if (tarea.getFechaLimiteStr() != null && !tarea.getFechaLimiteStr().isEmpty()) {
            try {
                LocalDate fecha = LocalDate.parse(tarea.getFechaLimiteStr());
                tarea.setFechaLimite(fecha);
                System.out.println("Fecha límite convertida: " + fecha);
            } catch (Exception e) {
                System.err.println("Error al convertir la fecha: " + e.getMessage());
                model.addAttribute("error", "Formato de fecha inválido");
                return "tareas/crear";
            }
        }

        Tarea nuevaTarea = tareaApiService.crearTarea(tarea);

        if (nuevaTarea != null) {

            redirectAttributes.addAttribute("mensaje", "Tarea creada correctamente");
            return "redirect:/tareas";
        } else {

            model.addAttribute("error", "Error al crear la tarea");
            return "tareas/crear";
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