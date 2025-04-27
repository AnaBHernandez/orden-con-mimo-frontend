package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ordenconmimo.orden_con_mimo_frontend.models.Espacio;
import com.ordenconmimo.orden_con_mimo_frontend.services.EspacioApiService;

@Controller
@RequestMapping("/espacios")
public class EspacioController {

    private final EspacioApiService espacioApiService;

    public EspacioController(EspacioApiService espacioApiService) {
        this.espacioApiService = espacioApiService;
    }

    @GetMapping({"", "/"})
    public String listarEspacios(Model model) {
        try {
            System.out.println("DEBUG - Iniciando listarEspacios()");
            List<Espacio> espacios = espacioApiService.obtenerEspacios();
            System.out.println("DEBUG - Espacios obtenidos: " + (espacios != null ? espacios.size() : "null"));

            model.addAttribute("espacios", espacios);
            model.addAttribute("title", "Mis Espacios MIMO");

            return "espacios/index";  // Cambiar esta línea
        } catch (Exception e) {
            System.out.println("ERROR - En listarEspacios: " + e.getMessage());
            e.printStackTrace();

            model.addAttribute("errorMessage", "Error al cargar los espacios: " + e.getMessage());
            model.addAttribute("espacios", Collections.emptyList());
            model.addAttribute("title", "Mis Espacios MIMO");

            return "espacios/index";  
        }
    }

    @GetMapping("/{id}")
    public String verEspacio(@PathVariable Long id, Model model) {
        Espacio espacio = espacioApiService.obtenerEspacioPorId(id);

        if (espacio == null) {
            return "redirect:/espacios?error=Espacio no encontrado";
        }

        model.addAttribute("espacio", espacio);
        model.addAttribute("tareas", espacioApiService.obtenerTareasPorEspacio(id));

        return "espacios/detalle";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoEspacio(Model model) {
        model.addAttribute("espacio", new Espacio());
        return "espacios/crear";
    }

    @GetMapping("/test-espacios")
    @ResponseBody
    public String testEspaciosApi() {
        try {
            List<Espacio> espacios = espacioApiService.obtenerEspacios();
            return "Conexión exitosa. Espacios recibidos: " + espacios.size();
        } catch (Exception e) {
            return "Error al obtener espacios: " + e.getMessage();
        }
    }

    @PostMapping("/guardar")
    public String guardarEspacio(@ModelAttribute Espacio espacio, RedirectAttributes redirectAttributes) {
        try {
            Espacio espacioCreado = espacioApiService.crearEspacio(espacio);

            if (espacioCreado != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Espacio creado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error al crear el espacio");
            }
            return "redirect:/espacios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear el espacio: " + e.getMessage());
            return "redirect:/espacios/nuevo";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarEspacioForm(@PathVariable Long id, Model model) {
        Espacio espacio = espacioApiService.obtenerEspacioPorId(id);

        if (espacio == null) {
            return "redirect:/espacios?error=Espacio no encontrado";
        }

        model.addAttribute("espacio", espacio);
        return "espacios/editar";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizarEspacio(@PathVariable Long id, @ModelAttribute Espacio espacio, RedirectAttributes redirectAttributes) {
        try {
            espacio.setId(id);
            Espacio espacioActualizado = espacioApiService.actualizarEspacio(espacio);

            if (espacioActualizado != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Espacio actualizado correctamente");
                return "redirect:/espacios";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar el espacio");
                return "redirect:/espacios/" + id + "/editar";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar el espacio: " + e.getMessage());
            return "redirect:/espacios/" + id + "/editar";
        }
    }

    @GetMapping("/{id}/eliminar")
    public String eliminarEspacio(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean eliminado = espacioApiService.eliminarEspacio(id);

            if (eliminado) {
                redirectAttributes.addFlashAttribute("successMessage", "Espacio eliminado correctamente");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo eliminar el espacio");
            }
            return "redirect:/espacios";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el espacio: " + e.getMessage());
            return "redirect:/espacios";
        }
    }

}
