package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.ordenconmimo.orden_con_mimo_frontend.models.Espacio;
import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.EspacioApiService;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("all")
class EspacioControllerTest {

    @Mock
    private EspacioApiService espacioApiService;
    
    @Mock
    private Model model;
    
    @Mock
    private RedirectAttributes redirectAttributes;
    
    @InjectMocks
    private EspacioController espacioController;
    
    private List<Espacio> espaciosMock;
    private Espacio espacioMock;
    private List<Tarea> tareasMock;
    
    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        espaciosMock = new ArrayList<>();
        
        Espacio espacio1 = new Espacio();
        espacio1.setId(1L);
        espacio1.setNombre("Espacio 1");
        espacio1.setDescripcion("Descripción 1");
        espacio1.setTipo("TRABAJO");
        
        Espacio espacio2 = new Espacio();
        espacio2.setId(2L);
        espacio2.setNombre("Espacio 2");
        espacio2.setDescripcion("Descripción 2");
        espacio2.setTipo("HOGAR");
        
        espaciosMock.add(espacio1);
        espaciosMock.add(espacio2);
        
        espacioMock = new Espacio();
        espacioMock.setId(3L);
        espacioMock.setNombre("Espacio 3");
        espacioMock.setDescripcion("Descripción 3");
        espacioMock.setTipo("OCIO");
        
        tareasMock = new ArrayList<>();
        Tarea tarea1 = new Tarea();
        tarea1.setId(1L);
        tarea1.setTitulo("Tarea 1");
        tarea1.setCategoria("MIRATE");
        
        Tarea tarea2 = new Tarea();
        tarea2.setId(2L);
        tarea2.setTitulo("Tarea 2");
        tarea2.setCategoria("ORDENA");
        
        tareasMock.add(tarea1);
        tareasMock.add(tarea2);
    }
    
    @Test
    void listarEspacios_DeberiaRetornarTodosLosEspacios() {
        // Configurar mock
        when(espacioApiService.obtenerEspacios()).thenReturn(espaciosMock);
        
        // Ejecutar método a probar
        String viewName = espacioController.listarEspacios(model);
        
        // Verificar resultados
        verify(model, times(1)).addAttribute(eq("espacios"), eq(espaciosMock));
        verify(model, times(1)).addAttribute(eq("title"), anyString());
        assertEquals("espacios/index", viewName);
    }
    
    @Test
    void listarEspacios_CuandoOcurreError_DeberiaManejarlo() {
        // Configurar mock para lanzar excepción
        when(espacioApiService.obtenerEspacios()).thenThrow(new RuntimeException("Error de prueba"));
        
        // Ejecutar método a probar
        String viewName = espacioController.listarEspacios(model);
        
        // Verificar resultados
        verify(model, times(1)).addAttribute(eq("errorMessage"), anyString());
        verify(model, times(1)).addAttribute(eq("espacios"), any());
        assertEquals("espacios/index", viewName);
    }
    
    @Test
    void verEspacio_DeberiaRetornarEspacioYSusTareas() {
        // Configurar mock
        when(espacioApiService.obtenerEspacioPorId(1L)).thenReturn(espacioMock);
        when(espacioApiService.obtenerTareasPorEspacio(1L)).thenReturn(tareasMock);
        
        // Ejecutar método a probar
        String viewName = espacioController.verEspacio(1L, model);
        
        // Verificar resultados
        verify(model, times(1)).addAttribute(eq("espacio"), eq(espacioMock));
        verify(model, times(1)).addAttribute(eq("tareas"), eq(tareasMock));
        assertEquals("espacios/detalle", viewName);
    }
    
    @Test
    void verEspacio_CuandoEspacioNoExiste_DeberiaRedireccionar() {
        // Configurar mock
        when(espacioApiService.obtenerEspacioPorId(999L)).thenReturn(null);
        
        // Ejecutar método a probar
        String viewName = espacioController.verEspacio(999L, model);
        
        // Verificar resultados
        assertEquals("redirect:/espacios?error=Espacio no encontrado", viewName);
    }
    
    @Test
    void mostrarFormularioNuevoEspacio_DeberiaRetornarFormulario() {
        // Ejecutar método a probar
        String viewName = espacioController.mostrarFormularioNuevoEspacio(model);
        
        // Verificar resultados
        verify(model, times(1)).addAttribute(eq("espacio"), any(Espacio.class));
        assertEquals("espacios/crear", viewName);
    }
    
    @Test
    void testEspaciosApi_DeberiaRetornarMensajeExito() {
        // Configurar mock
        when(espacioApiService.obtenerEspacios()).thenReturn(espaciosMock);
        
        // Ejecutar método a probar
        String response = espacioController.testEspaciosApi();
        
        // Verificar resultados
        assertEquals("Conexión exitosa. Espacios recibidos: 2", response);
    }
    
    @Test
    void testEspaciosApi_CuandoHayError_DeberiaRetornarMensajeError() {
        // Configurar mock para lanzar excepción
        when(espacioApiService.obtenerEspacios()).thenThrow(new RuntimeException("Error de prueba"));
        
        // Ejecutar método a probar
        String response = espacioController.testEspaciosApi();
        
        // Verificar resultados
        assertEquals("Error al obtener espacios: Error de prueba", response);
    }
    
    @Test
    void guardarEspacio_DeberiaCrearEspacio() {
        // Configurar mock
        when(espacioApiService.crearEspacio(any(Espacio.class))).thenReturn(espacioMock);
        
        // Ejecutar método a probar
        String viewName = espacioController.guardarEspacio(new Espacio(), redirectAttributes);
        
        // Verificar resultados
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("successMessage"), anyString());
        assertEquals("redirect:/espacios", viewName);
    }
    
    @Test
    void guardarEspacio_CuandoHayError_DeberiaManejarlo() {
        // Configurar mock para lanzar excepción
        when(espacioApiService.crearEspacio(any(Espacio.class))).thenThrow(new RuntimeException("Error de prueba"));
        
        // Ejecutar método a probar
        String viewName = espacioController.guardarEspacio(new Espacio(), redirectAttributes);
        
        // Verificar resultados
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("errorMessage"), anyString());
        assertEquals("redirect:/espacios/nuevo", viewName);
    }
    
    @Test
    void editarEspacioForm_DeberiaRetornarFormularioEdicion() {
        // Configurar mock
        when(espacioApiService.obtenerEspacioPorId(1L)).thenReturn(espacioMock);
        
        // Ejecutar método a probar
        String viewName = espacioController.editarEspacioForm(1L, model);
        
        // Verificar resultados
        verify(model, times(1)).addAttribute(eq("espacio"), eq(espacioMock));
        assertEquals("espacios/editar", viewName);
    }
    
    @Test
    void editarEspacioForm_CuandoEspacioNoExiste_DeberiaRedireccionar() {
        // Configurar mock
        when(espacioApiService.obtenerEspacioPorId(999L)).thenReturn(null);
        
        // Ejecutar método a probar
        String viewName = espacioController.editarEspacioForm(999L, model);
        
        // Verificar resultados
        assertEquals("redirect:/espacios?error=Espacio no encontrado", viewName);
    }
    
    @Test
    void actualizarEspacio_DeberiaActualizarEspacio() {
        // Configurar mock
        Espacio espacioParaActualizar = new Espacio();
        when(espacioApiService.actualizarEspacio(any(Espacio.class))).thenReturn(espacioMock);
        
        // Ejecutar método a probar
        String viewName = espacioController.actualizarEspacio(1L, espacioParaActualizar, redirectAttributes);
        
        // Verificar resultados
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("successMessage"), anyString());
        assertEquals("redirect:/espacios", viewName);
    }
    
    @Test
    void actualizarEspacio_CuandoActualizacionFalla_DeberiaRedirigirAFormulario() {
        // Configurar mock para retornar null (indicando fallo)
        when(espacioApiService.actualizarEspacio(any(Espacio.class))).thenReturn(null);
        
        // Ejecutar método a probar
        String viewName = espacioController.actualizarEspacio(1L, new Espacio(), redirectAttributes);
        
        // Verificar resultados
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("errorMessage"), anyString());
        assertEquals("redirect:/espacios/1/editar", viewName);
    }
    
    @Test
    void actualizarEspacio_CuandoHayError_DeberiaManejarlo() {
        // Configurar mock para lanzar excepción
        when(espacioApiService.actualizarEspacio(any(Espacio.class))).thenThrow(new RuntimeException("Error de prueba"));
        
        // Ejecutar método a probar
        String viewName = espacioController.actualizarEspacio(1L, new Espacio(), redirectAttributes);
        
        // Verificar resultados
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("errorMessage"), anyString());
        assertEquals("redirect:/espacios/1/editar", viewName);
    }
    
    @Test
    void eliminarEspacio_DeberiaEliminarEspacio() {
        // Configurar mock
        when(espacioApiService.eliminarEspacio(1L)).thenReturn(true);
        
        // Ejecutar método a probar
        String viewName = espacioController.eliminarEspacio(1L, redirectAttributes);
        
        // Verificar resultados
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("successMessage"), anyString());
        assertEquals("redirect:/espacios", viewName);
    }
    
    @Test
    void eliminarEspacio_CuandoEliminacionFalla_DeberiaManejarlo() {
        // Configurar mock para retornar false (indicando fallo)
        when(espacioApiService.eliminarEspacio(1L)).thenReturn(false);
        
        // Ejecutar método a probar
        String viewName = espacioController.eliminarEspacio(1L, redirectAttributes);
        
        // Verificar resultados
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("errorMessage"), anyString());
        assertEquals("redirect:/espacios", viewName);
    }
    
    @Test
    void eliminarEspacio_CuandoHayError_DeberiaManejarlo() {
        // Configurar mock para lanzar excepción
        when(espacioApiService.eliminarEspacio(1L)).thenThrow(new RuntimeException("Error de prueba"));
        
        // Ejecutar método a probar
        String viewName = espacioController.eliminarEspacio(1L, redirectAttributes);
        
        // Verificar resultados
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("errorMessage"), anyString());
        assertEquals("redirect:/espacios", viewName);
    }
}