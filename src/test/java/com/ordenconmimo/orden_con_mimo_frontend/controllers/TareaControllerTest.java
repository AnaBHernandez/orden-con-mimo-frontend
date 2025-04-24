package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TareaControllerTest {

    @Mock
    private TareaApiService tareaApiService;

    @InjectMocks
    private TareaController tareaController;

    private MockMvc mockMvc;
    private Tarea tareaEjemplo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tareaController).build();

        tareaEjemplo = new Tarea(1L, "Test Tarea", "Descripción de prueba", "MIRATE", LocalDate.now(), false);
    }

    @Test
    public void testListarTareas() throws Exception {
        List<Tarea> tareas = Arrays.asList(
                tareaEjemplo,
                new Tarea(2L, "Test2", "Descripción2", "IMAGINA", LocalDate.now(), true));

        when(tareaApiService.obtenerTareas()).thenReturn(tareas);

        mockMvc.perform(get("/tareas"))
                .andExpect(status().isOk())
                .andExpect(view().name("tarea/list"))
                .andExpect(model().attributeExists("tareas"));
    }

    @Test
    public void testListarTareasConError() throws Exception {
        when(tareaApiService.obtenerTareas()).thenThrow(new RuntimeException("Error de prueba"));

        mockMvc.perform(get("/tareas"))
                .andExpect(status().isOk())
                .andExpect(view().name("tarea/list"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    public void testEditarTareaForm() throws Exception {
        when(tareaApiService.obtenerTareaPorId(1L)).thenReturn(tareaEjemplo);

        mockMvc.perform(get("/tareas/1/editar"))
                .andExpect(status().isOk())
                .andExpect(view().name("tarea/editar"))
                .andExpect(model().attributeExists("tarea"));
    }

    @Test
    public void testEditarTareaFormNotFound() throws Exception {
        when(tareaApiService.obtenerTareaPorId(999L)).thenReturn(null);

        mockMvc.perform(get("/tareas/999/editar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/tareas?error=*"));
    }

    @Test
    public void testEliminarTarea() throws Exception {
        when(tareaApiService.eliminarTarea(1L)).thenReturn(true);

        mockMvc.perform(get("/tareas/1/eliminar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/tareas?mensaje=*"));
    }

    @Test
    public void testEliminarTareaError() throws Exception {
        when(tareaApiService.eliminarTarea(1L)).thenReturn(false);

        mockMvc.perform(get("/tareas/1/eliminar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/tareas?error=*"));
    }

    @Test
    public void testMostrarFormularioNuevaTarea() throws Exception {
        mockMvc.perform(get("/tareas/nueva"))
                .andExpect(status().isOk())
                .andExpect(view().name("tarea/crear"))
                .andExpect(model().attributeExists("tarea"));
    }

    @Test
    public void testGuardarTarea() {
        Tarea tarea = new Tarea();
        tarea.setTitulo("Test Tarea");
        tarea.setDescripcion("Descripción de prueba");
        tarea.setCategoria("MIRATE");
        tarea.setFechaLimiteStr("2025-04-30");

        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        Tarea tareaCreada = new Tarea();
        tareaCreada.setId(1L);
        tareaCreada.setTitulo("Test Tarea");

        when(tareaApiService.crearTarea(any(Tarea.class))).thenReturn(tareaCreada);

        String resultado = tareaController.guardarTareaNueva(tarea, redirectAttributes);

        verify(tareaApiService).crearTarea(any(Tarea.class));

        assertEquals("redirect:/tareas", resultado);
    }

    @Test
    public void testGuardarTareaError() throws Exception {
        when(tareaApiService.guardarTarea(any(Tarea.class))).thenReturn(null);

        mockMvc.perform(post("/tareas/guardar")
                .param("titulo", "Nueva Tarea")
                .param("descripcion", "Descripción")
                .param("categoria", "ORDENA"))
                .andExpect(status().isOk())
                .andExpect(view().name("tarea/crear"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testActualizarTarea() throws Exception {
        Tarea tareaActualizada = new Tarea(1L, "Actualizada", "Nueva descripción", "MUEVETE", LocalDate.now(), true);

        when(tareaApiService.actualizarTarea(any(Tarea.class))).thenReturn(tareaActualizada);

        mockMvc.perform(post("/tareas/1/actualizar")
                .param("id", "1")
                .param("titulo", "Actualizada")
                .param("descripcion", "Nueva descripción")
                .param("categoria", "MUEVETE")
                .param("completada", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/tareas?mensaje=*"));
    }

    @Test
    public void testActualizarTareaError() throws Exception {
        when(tareaApiService.actualizarTarea(any(Tarea.class))).thenReturn(null);

        mockMvc.perform(post("/tareas/1/actualizar")
                .param("id", "1")
                .param("titulo", "Actualizada")
                .param("descripcion", "Nueva descripción")
                .param("categoria", "MUEVETE"))
                .andExpect(status().isOk())
                .andExpect(view().name("tarea/editar"))
                .andExpect(model().attributeExists("error"));
    }
}
