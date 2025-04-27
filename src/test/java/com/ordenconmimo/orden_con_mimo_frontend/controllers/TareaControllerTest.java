package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("all")
class TareaControllerTest {

    @Mock
    private TareaApiService tareaApiService;
    
    @Mock
    private Model model;
    
    @Mock
    private RedirectAttributes redirectAttributes;
    
    @InjectMocks
    private TareaController tareaController;
    
    private List<Tarea> tareasMock;
    private Tarea tareaMock;
    
    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        tareasMock = new ArrayList<>();
        
        Tarea tarea1 = new Tarea();
        tarea1.setId(1L);
        tarea1.setTitulo("Tarea 1");
        tarea1.setDescripcion("Descripción 1");
        tarea1.setCategoria("MIRATE");
        tarea1.setCompletada(false);
        
        Tarea tarea2 = new Tarea();
        tarea2.setId(2L);
        tarea2.setTitulo("Tarea 2");
        tarea2.setDescripcion("Descripción 2");
        tarea2.setCategoria("ORDENA");
        tarea2.setCompletada(true);
        
        tareasMock.add(tarea1);
        tareasMock.add(tarea2);
        
        tareaMock = new Tarea();
        tareaMock.setId(3L);
        tareaMock.setTitulo("Tarea 3");
        tareaMock.setDescripcion("Descripción 3");
        tareaMock.setCategoria("IMAGINA");
        tareaMock.setCompletada(false);
    }
    
    @Test
    void listarTareas_DeberiaRetornarTodasLasTareas() {
        // Configurar mock
        when(tareaApiService.obtenerTareas()).thenReturn(tareasMock);
        
        // Ejecutar método a probar
        String viewName = tareaController.listarTareas(null, model);
        
        // Verificar resultados
        verify(model, times(1)).addAttribute(eq("tareas"), eq(tareasMock));
        verify(model, times(1)).addAttribute(eq("title"), anyString());
        
        // Verificar que se retorna la vista correcta
        assert viewName.equals("tarea/list");
    }
    
    @Test
    void listarTareas_ConFiltroCategoria_DeberiaFiltrarTareas() {
        // Configurar mock
        when(tareaApiService.obtenerTareas()).thenReturn(tareasMock);
        
        // Ejecutar método a probar
        String viewName = tareaController.listarTareas("MIRATE", model);
        
        // Verificar resultados
        verify(model, times(1)).addAttribute(eq("categoriaActual"), eq("MIRATE"));
        
        // Verificar que se retorna la vista correcta
        assert viewName.equals("tarea/list");
    }
    
    @Test
    void listarTareas_CuandoOcurreError_DeberiaManejarlo() {
        // Configurar mock para lanzar excepción
        when(tareaApiService.obtenerTareas()).thenThrow(new RuntimeException("Error de prueba"));
        
        // Ejecutar método a probar
        String viewName = tareaController.listarTareas(null, model);
        
        // Verificar resultados
        verify(model, times(1)).addAttribute(eq("errorMessage"), anyString());
        
        // Verificar que se retorna la vista correcta
        assert viewName.equals("tarea/list");
    }
    
    @Test
    void editarTareaForm_DeberiaRetornarFormularioEdicion() {
        // Configurar mock
        when(tareaApiService.obtenerTareaPorId(1L)).thenReturn(tareaMock);
        
        // Ejecutar método a probar
        String viewName = tareaController.editarTareaForm(1L, model);
        
        // Verificar resultados
        verify(model, times(1)).addAttribute(eq("tarea"), any(Tarea.class));
        
        // Verificar que se retorna la vista correcta
        assert viewName.equals("tarea/editar");
    }
    
    @Test
    void editarTareaForm_CuandoTareaNoExiste_DeberiaRedireccionar() {
        // Configurar mock para retornar null
        when(tareaApiService.obtenerTareaPorId(999L)).thenReturn(null);
        
        // Ejecutar método a probar
        String viewName = tareaController.editarTareaForm(999L, model);
        
        // Verificar que se redirecciona correctamente
        assert viewName.startsWith("redirect:/tareas");
    }
    
    @Test
    void guardarTarea_DeberiaCrearTarea() {
        // Configurar mock
        when(tareaApiService.crearTarea(any(Tarea.class))).thenReturn(tareaMock);
        
        // Ejecutar método a probar
        String viewName = tareaController.guardarTarea(new Tarea(), redirectAttributes);
        
        // Verificar resultados
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("successMessage"), anyString());
        
        // Verificar que se redirecciona correctamente
        assert viewName.equals("redirect:/tareas");
    }
    
    @Test
    void guardarTarea_CuandoHayError_DeberiaManejarlo() {
        // Configurar mock para lanzar excepción
        when(tareaApiService.crearTarea(any(Tarea.class))).thenThrow(new RuntimeException("Error de prueba"));
        
        // Ejecutar método a probar
        String viewName = tareaController.guardarTarea(new Tarea(), redirectAttributes);
        
        // Verificar resultados
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("errorMessage"), anyString());
        
        // Verificar que se redirecciona correctamente
        assert viewName.equals("redirect:/tareas/nueva");
    }
    
    @Test
    void eliminarTarea_DeberiaEliminarTarea() {
        // Configurar mock
        doNothing().when(tareaApiService).eliminarTarea(anyLong());
        
        // Ejecutar método a probar
        String viewName = tareaController.eliminarTarea(1L);
        
        // Verificar que se llama al servicio
        verify(tareaApiService, times(1)).eliminarTarea(1L);
        
        // Verificar que se redirecciona correctamente
        assert viewName.startsWith("redirect:/tareas");
    }
    
    @Test
    void eliminarTarea_CuandoHayError_DeberiaManejarlo() {
        // Configurar mock para lanzar excepción
        doNothing().when(tareaApiService).eliminarTarea(anyLong());
        
        // Ejecutar método a probar
        String viewName = tareaController.eliminarTarea(1L);
        
        // Verificar que se redirecciona correctamente
        assert viewName.startsWith("redirect:/tareas");
    }
    
    @Test
    void mostrarFormularioNuevaTarea_DeberiaRetornarFormulario() {
        // Ejecutar método a probar
        String viewName = tareaController.mostrarFormularioNuevaTarea(model);
        
        // Verificar resultados
        verify(model, times(1)).addAttribute(eq("tarea"), any(Tarea.class));
        
        // Verificar que se retorna la vista correcta
        assert viewName.equals("tarea/crear");
    }
    
    @Test
    void guardarTareaNueva_DeberiaCrearTarea() {
        // Configurar mock
        when(tareaApiService.crearTarea(any(Tarea.class))).thenReturn(tareaMock);
        
        // Ejecutar método a probar
        String viewName = tareaController.guardarTareaNueva(new Tarea(), redirectAttributes);
        
        // Verificar resultados
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("successMessage"), anyString());
        
        // Verificar que se redirecciona correctamente
        assert viewName.equals("redirect:/tareas");
    }
    
    @Test
    void actualizarTarea_DeberiaActualizarTarea() {
        // Configurar mock
        when(tareaApiService.actualizarTarea(any(Tarea.class))).thenReturn(tareaMock);
        
        // Ejecutar método a probar
        String viewName = tareaController.actualizarTarea(1L, new Tarea(), model);
        
        // Verificar que se redirecciona correctamente
        assert viewName.startsWith("redirect:/tareas");
    }
    
    @Test
    void actualizarTarea_CuandoHayError_DeberiaManejarlo() {
        // Configurar mock para lanzar excepción
        when(tareaApiService.actualizarTarea(any(Tarea.class))).thenThrow(new RuntimeException("Error de prueba"));
        
        // Ejecutar método a probar
        String viewName = tareaController.actualizarTarea(1L, new Tarea(), model);
        
        // Verificar resultados
        verify(model, times(1)).addAttribute(eq("error"), anyString());
        verify(model, times(1)).addAttribute(eq("tarea"), any(Tarea.class));
        
        // Verificar que se retorna la vista correcta
        assert viewName.equals("tarea/editar");
    }
}