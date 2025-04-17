package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TareaControllerTest {

    @Mock
    private TareaApiService tareaApiService;

    @InjectMocks
    private TareaController tareaController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Tarea tareaEjemplo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tareaController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // Para manejar LocalDate
        
        tareaEjemplo = new Tarea(1L, "Test Tarea", "Descripción de prueba", "MIRATE", LocalDate.now(), false);
    }

    @Test
    public void testListarTareas() throws Exception {
        List<Tarea> tareas = Arrays.asList(
            tareaEjemplo,
            new Tarea(2L, "Test2", "Descripción2", "IMAGINA", LocalDate.now(), true)
        );
        
        when(tareaApiService.obtenerTareas()).thenReturn(tareas);
        
        mockMvc.perform(get("/api/tareas"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));
        
        verify(tareaApiService, times(1)).obtenerTareas();
    }
    
    @Test
    public void testObtenerTarea() throws Exception {
        when(tareaApiService.obtenerTareaPorId(1L)).thenReturn(tareaEjemplo);
        
        mockMvc.perform(get("/api/tareas/1"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.titulo").value("Test Tarea"));
        
        verify(tareaApiService, times(1)).obtenerTareaPorId(1L);
    }
    
    @Test
    public void testObtenerTarea_NotFound() throws Exception {
        when(tareaApiService.obtenerTareaPorId(999L)).thenReturn(null);
        
        mockMvc.perform(get("/api/tareas/999"))
            .andExpect(status().isNotFound());
        
        verify(tareaApiService, times(1)).obtenerTareaPorId(999L);
    }
    
    @Test
    public void testCrearTarea() throws Exception {
        // Arrange
        Tarea nuevaTarea = new Tarea(null, "Nueva", "Descripción", "ORDENA", LocalDate.now(), false);
        Tarea tareaGuardada = new Tarea(3L, "Nueva", "Descripción", "ORDENA", LocalDate.now(), false);
        
        when(tareaApiService.guardarTarea(any(Tarea.class))).thenReturn(tareaGuardada);
        
        mockMvc.perform(post("/api/tareas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nuevaTarea)))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.titulo").value("Nueva"));
        
        verify(tareaApiService, times(1)).guardarTarea(any(Tarea.class));
    }
    
    @Test
    public void testActualizarTarea() throws Exception {
        Tarea tareaActualizada = new Tarea(1L, "Actualizada", "Nueva descripción", "MUEVETE", LocalDate.now(), true);
        
        when(tareaApiService.actualizarTarea(any(Tarea.class))).thenReturn(tareaActualizada);
        
        mockMvc.perform(put("/api/tareas/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(tareaActualizada)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.titulo").value("Actualizada"))
            .andExpect(jsonPath("$.completada").value(true));
        
        verify(tareaApiService, times(1)).actualizarTarea(any(Tarea.class));
    }
    
    @Test
    public void testActualizarTarea_IdMismatch() throws Exception {
        Tarea tareaActualizada = new Tarea(1L, "Actualizada", "Nueva descripción", "MUEVETE", LocalDate.now(), true);
        
        mockMvc.perform(put("/api/tareas/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(tareaActualizada)))
            .andExpect(status().isBadRequest());
        
        verify(tareaApiService, never()).actualizarTarea(any(Tarea.class));
    }
    
    @Test
    public void testEliminarTarea() throws Exception {
        doNothing().when(tareaApiService).eliminarTarea(anyLong());
        
        mockMvc.perform(delete("/api/tareas/1"))
            .andExpect(status().isNoContent());
        
        verify(tareaApiService, times(1)).eliminarTarea(1L);
    }
}