package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;

@Controller
@RequestMapping("/tareas")
public class TareaController {

    private final TareaApiService tareaApiService;

    public TareaController(TareaApiService tareaApiService) {
        this.tareaApiService = tareaApiService;
    }

    @GetMapping
    public String listarTareas(@RequestParam(required = false) String categoria, Model model) {
        try {
            List<Tarea> tareas = tareaApiService.obtenerTareas();

            if (categoria != null && !categoria.isEmpty()) {
                tareas = tareas.stream()
                        .filter(t -> categoria.equals(t.getCategoria()))
                        .collect(Collectors.toList());
                model.addAttribute("categoriaActual", categoria);
            }

            model.addAttribute("tareas", tareas);
            model.addAttribute("title", "Mis Tareas del Reino");
            return "tarea/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar las tareas: " + e.getMessage());
            model.addAttribute("tareas", Collections.emptyList());
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

    @PostMapping
    public String guardarTarea(@ModelAttribute Tarea tarea, RedirectAttributes redirectAttributes) {
        System.out.println("Método guardarTarea llamado");
        try {
            Tarea tareaCreada = tareaApiService.crearTarea(tarea);
            
            if (tareaCreada != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Tarea guardada exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la tarea");
            }
            return "redirect:/tareas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar la tarea: " + e.getMessage());
            return "redirect:/tareas/nueva";
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
    public String guardarTareaNueva(@ModelAttribute Tarea tarea, RedirectAttributes redirectAttributes) {
        try {
            Tarea tareaCreada = tareaApiService.crearTarea(tarea);
            
            if (tareaCreada != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Tarea creada exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error al crear la tarea");
            }
            return "redirect:/tareas";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear la tarea: " + e.getMessage());
            return "redirect:/tareas/nueva";
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