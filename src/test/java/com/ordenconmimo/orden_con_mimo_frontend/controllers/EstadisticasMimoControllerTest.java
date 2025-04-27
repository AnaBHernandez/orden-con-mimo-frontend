package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;

@ExtendWith(MockitoExtension.class)
public class EstadisticasMimoControllerTest {

    @InjectMocks
    private EstadisticasMimoController estadisticasMimoController;

    @Mock
    private TareaApiService tareaApiService;

    @Mock
    private Model model;

    private List<Tarea> tareasEjemplo;
    private Map<String, Integer> conteosEjemplo;

    @BeforeEach
    void setUp() {
        Tarea tarea1 = new Tarea();
        tarea1.setId(1L);
        tarea1.setTitulo("Tarea Mírate 1");
        tarea1.setCategoria("MIRATE");

        Tarea tarea2 = new Tarea();
        tarea2.setId(2L);
        tarea2.setTitulo("Tarea Mírate 2");
        tarea2.setCategoria("MIRATE");

        Tarea tarea3 = new Tarea();
        tarea3.setId(3L);
        tarea3.setTitulo("Tarea Imagina");
        tarea3.setCategoria("IMAGINA");

        Tarea tarea4 = new Tarea();
        tarea4.setId(4L);
        tarea4.setTitulo("Tarea Muévete");
        tarea4.setCategoria("MUEVETE");

        Tarea tarea5 = new Tarea();
        tarea5.setId(5L);
        tarea5.setTitulo("Tarea Ordena");
        tarea5.setCategoria("ORDENA");

        tareasEjemplo = Arrays.asList(tarea1, tarea2, tarea3, tarea4, tarea5);

        conteosEjemplo = new HashMap<>();
        conteosEjemplo.put("MIRATE", 2);
        conteosEjemplo.put("IMAGINA", 1);
        conteosEjemplo.put("MUEVETE", 1);
        conteosEjemplo.put("ORDENA", 1);
    }

    @Test
    void testMostrarEstadisticas() {
        when(tareaApiService.obtenerTareas()).thenReturn(tareasEjemplo);

        String viewName = estadisticasMimoController.mostrarEstadisticas(model);

        assertEquals("estadisticas", viewName);
        verify(model).addAttribute("title", "Estadísticas MIMO");
        verify(model).addAttribute(eq("estadisticas"), eq(conteosEjemplo));
        verify(tareaApiService, times(1)).obtenerTareas();
    }

    @Test
    void testMostrarEstadisticasConTareasNull() {
        when(tareaApiService.obtenerTareas()).thenReturn(null);

        Map<String, Integer> conteosVaciosLocales = new HashMap<>();
        conteosVaciosLocales.put("MIRATE", 0);
        conteosVaciosLocales.put("IMAGINA", 0);
        conteosVaciosLocales.put("MUEVETE", 0);
        conteosVaciosLocales.put("ORDENA", 0);

        String viewName = estadisticasMimoController.mostrarEstadisticas(model);

        assertEquals("estadisticas", viewName);
        verify(model).addAttribute("title", "Estadísticas MIMO");
        verify(model).addAttribute(eq("estadisticas"), eq(conteosVaciosLocales));
        verify(tareaApiService, times(1)).obtenerTareas();
    }

    @Test
    void testMostrarEstadisticasConExcepcion() {
        when(tareaApiService.obtenerTareas()).thenThrow(new RuntimeException("Error de conexión"));

        Map<String, Integer> conteosVaciosLocales = new HashMap<>();
        conteosVaciosLocales.put("MIRATE", 0);
        conteosVaciosLocales.put("IMAGINA", 0);
        conteosVaciosLocales.put("MUEVETE", 0);
        conteosVaciosLocales.put("ORDENA", 0);

        String viewName = estadisticasMimoController.mostrarEstadisticas(model);

        assertEquals("estadisticas", viewName);
        verify(model).addAttribute("title", "Estadísticas MIMO - Error");
        verify(model).addAttribute(eq("error"), contains("Error de conexión"));
        verify(model).addAttribute(eq("estadisticas"), any(Map.class));
        verify(tareaApiService, times(1)).obtenerTareas();
    }

    @Test
    void testMostrarEstadisticasConCategoriasNull() {
        Tarea tareaNull = new Tarea();
        tareaNull.setId(1L);
        tareaNull.setTitulo("Tarea Sin Categoría");
        tareaNull.setCategoria(null);

        List<Tarea> tareasConNull = Arrays.asList(
            tareasEjemplo.get(0),
            tareaNull,
            tareasEjemplo.get(2)
        );

        when(tareaApiService.obtenerTareas()).thenReturn(tareasConNull);

        Map<String, Integer> conteosEsperadosLocales = new HashMap<>();
        conteosEsperadosLocales.put("MIRATE", 1);
        conteosEsperadosLocales.put("IMAGINA", 1);
        conteosEsperadosLocales.put("MUEVETE", 0);
        conteosEsperadosLocales.put("ORDENA", 0);

        String viewName = estadisticasMimoController.mostrarEstadisticas(model);

        assertEquals("estadisticas", viewName);
        verify(model).addAttribute(eq("estadisticas"), eq(conteosEsperadosLocales));  
        verify(tareaApiService, times(1)).obtenerTareas();
    }
}