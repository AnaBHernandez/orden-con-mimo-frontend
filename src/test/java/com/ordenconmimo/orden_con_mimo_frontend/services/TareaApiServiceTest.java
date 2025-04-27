package com.ordenconmimo.orden_con_mimo_frontend.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SuppressWarnings("all")
class TareaApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TareaApiService tareaApiService;

    @Test
    void obtenerTareas_DeberiaRetornarListaDeTareas() {
        // Preparar datos de prueba
        List<Map<String, Object>> mockResponse = new ArrayList<>();
        Map<String, Object> tarea1 = new HashMap<>();
        tarea1.put("id", 1);
        tarea1.put("nombre", "Tarea 1");
        tarea1.put("descripcion", "Descripción 1");
        tarea1.put("categoria", "MIRATE");
        tarea1.put("completada", false);
        
        Map<String, Object> tarea2 = new HashMap<>();
        tarea2.put("id", 2);
        tarea2.put("nombre", "Tarea 2");
        tarea2.put("descripcion", "Descripción 2");
        tarea2.put("categoria", "ORDENA");
        tarea2.put("completada", true);
        
        mockResponse.add(tarea1);
        mockResponse.add(tarea2);
        
        // Configurar mock
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        
        // Ejecutar método a probar
        List<Tarea> resultado = tareaApiService.obtenerTareas();
        
        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Tarea 1", resultado.get(0).getTitulo());
        assertEquals("MIRATE", resultado.get(0).getCategoria());
        assertEquals("Tarea 2", resultado.get(1).getTitulo());
        assertEquals("ORDENA", resultado.get(1).getCategoria());
    }
    
    @Test
    void obtenerTareas_CuandoOcurreError_DeberiaRetornarListaVacia() {
        // Configurar mock para simular error
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class)))
                .thenThrow(new ResourceAccessException("Error de conexión"));
        
        // Ejecutar método a probar
        List<Tarea> resultado = tareaApiService.obtenerTareas();
        
        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }
    
    @Test
    void obtenerTareaPorId_DeberiaRetornarTarea() {
        // Preparar datos de prueba
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("id", 1);
        mockResponse.put("nombre", "Tarea 1");
        mockResponse.put("descripcion", "Descripción 1");
        mockResponse.put("categoria", "MIRATE");
        mockResponse.put("completada", false);
        
        // Configurar mock
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        
        // Ejecutar método a probar
        Tarea resultado = tareaApiService.obtenerTareaPorId(1L);
        
        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Tarea 1", resultado.getTitulo());
        assertEquals("Descripción 1", resultado.getDescripcion());
        assertEquals("MIRATE", resultado.getCategoria());
        assertEquals(false, resultado.isCompletada());
    }
    
    @Test
    void obtenerTareaPorId_CuandoNoExiste_DeberiaRetornarNull() {
        // Configurar mock para simular error 404
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        // Ejecutar método a probar
        Tarea resultado = tareaApiService.obtenerTareaPorId(999L);
        
        // Verificar resultados
        assertNull(resultado);
    }
    
    @Test
    void crearTarea_DeberiaRetornarTareaCreada() {
        // Preparar datos de prueba
        Tarea tareaACrear = new Tarea();
        tareaACrear.setTitulo("Nueva Tarea");
        tareaACrear.setDescripcion("Nueva Descripción");
        tareaACrear.setCategoria("IMAGINA");
        tareaACrear.setCompletada(false);
        
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("id", 3);
        mockResponse.put("nombre", "Nueva Tarea");
        mockResponse.put("descripcion", "Nueva Descripción");
        mockResponse.put("categoria", "IMAGINA");
        mockResponse.put("completada", false);
        
        // Configurar mock
        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(Object.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.CREATED));
        
        // Ejecutar método a probar
        Tarea resultado = tareaApiService.crearTarea(tareaACrear);
        
        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Nueva Tarea", resultado.getTitulo());
        assertEquals("Nueva Descripción", resultado.getDescripcion());
        assertEquals("IMAGINA", resultado.getCategoria());
        assertEquals(false, resultado.isCompletada());
    }
    
    @Test
    void crearTarea_CuandoHayError_DeberiaLanzarExcepcion() {
        // Preparar datos de prueba
        Tarea tareaInvalida = new Tarea();
        
        // Configurar mock para simular error
        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(Object.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        
        // Verificar que se lanza excepción
        assertThrows(RuntimeException.class, () -> {
            tareaApiService.crearTarea(tareaInvalida);
        });
    }
    
    @Test
    void actualizarTarea_DeberiaRetornarTareaActualizada() {
        // Preparar datos de prueba
        Tarea tareaAActualizar = new Tarea();
        tareaAActualizar.setId(1L);
        tareaAActualizar.setTitulo("Tarea Actualizada");
        tareaAActualizar.setDescripcion("Descripción Actualizada");
        tareaAActualizar.setCategoria("MUEVETE");
        tareaAActualizar.setCompletada(true);
        
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("id", 1);
        mockResponse.put("nombre", "Tarea Actualizada");
        mockResponse.put("descripcion", "Descripción Actualizada");
        mockResponse.put("categoria", "MUEVETE");
        mockResponse.put("completada", true);
        
        // Configurar mock
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(),
                eq(Object.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        
        // Ejecutar método a probar
        Tarea resultado = tareaApiService.actualizarTarea(tareaAActualizar);
        
        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Tarea Actualizada", resultado.getTitulo());
        assertEquals("Descripción Actualizada", resultado.getDescripcion());
        assertEquals("MUEVETE", resultado.getCategoria());
        assertEquals(true, resultado.isCompletada());
    }
    
    @Test
    void actualizarTarea_CuandoHayError_DeberiaLanzarExcepcion() {
        // Preparar datos de prueba
        Tarea tareaInvalida = new Tarea();
        tareaInvalida.setId(999L);
        
        // Configurar mock para simular error
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(),
                eq(Object.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        
        // Verificar que se lanza excepción
        assertThrows(RuntimeException.class, () -> {
            tareaApiService.actualizarTarea(tareaInvalida);
        });
    }
    
    @Test
    void eliminarTarea_DeberiaEjecutarCorrectamente() {
        // Configurar mock
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.DELETE),
                any(),
                eq(Object.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
        
        // Ejecutar método a probar (no debería lanzar excepciones)
        tareaApiService.eliminarTarea(1L);
        
        // El test pasa si no hay excepciones
    }
    
    @Test
    void eliminarTarea_ConErrorDeConexion_NoDeberiaLanzarExcepcion() {
        // Configurar mock para simular error de conexión
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.DELETE),
                any(),
                eq(Object.class)))
                .thenThrow(new ResourceAccessException("Error de conexión"));
        
        // Ejecutar método a probar (no debería lanzar excepciones)
        tareaApiService.eliminarTarea(1L);
        
        // El test pasa si no hay excepciones
    }
    
    @Test
    void obtenerTareasPorCategoria_DeberiaRetornarTareasFiltradas() {
        // Preparar datos de prueba
        List<Map<String, Object>> mockResponse = new ArrayList<>();
        Map<String, Object> tarea1 = new HashMap<>();
        tarea1.put("id", 1);
        tarea1.put("nombre", "Tarea Mírate 1");
        tarea1.put("descripcion", "Descripción 1");
        tarea1.put("categoria", "MIRATE");
        tarea1.put("completada", false);
        
        Map<String, Object> tarea2 = new HashMap<>();
        tarea2.put("id", 2);
        tarea2.put("nombre", "Tarea Mírate 2");
        tarea2.put("descripcion", "Descripción 2");
        tarea2.put("categoria", "MIRATE");
        tarea2.put("completada", true);
        
        mockResponse.add(tarea1);
        mockResponse.add(tarea2);
        
        // Configurar mock
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        
        // Ejecutar método a probar
        List<Tarea> resultado = tareaApiService.obtenerTareasPorCategoria("MIRATE");
        
        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Tarea Mírate 1", resultado.get(0).getTitulo());
        assertEquals("MIRATE", resultado.get(0).getCategoria());
        assertEquals("Tarea Mírate 2", resultado.get(1).getTitulo());
        assertEquals("MIRATE", resultado.get(1).getCategoria());
    }
    
    @Test
    void obtenerTareasPorEspacio_DeberiaRetornarTareasFiltradas() {
        // Preparar datos de prueba
        List<Map<String, Object>> mockResponse = new ArrayList<>();
        Map<String, Object> tarea1 = new HashMap<>();
        tarea1.put("id", 1);
        tarea1.put("nombre", "Tarea Espacio 1");
        tarea1.put("descripcion", "Descripción 1");
        tarea1.put("categoria", "MIRATE");
        tarea1.put("completada", false);
        
        Map<String, Object> tarea2 = new HashMap<>();
        tarea2.put("id", 2);
        tarea2.put("nombre", "Tarea Espacio 2");
        tarea2.put("descripcion", "Descripción 2");
        tarea2.put("categoria", "ORDENA");
        tarea2.put("completada", true);
        
        mockResponse.add(tarea1);
        mockResponse.add(tarea2);
        
        // Configurar mock
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(Object.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));
        
        // Ejecutar método a probar
        List<Tarea> resultado = tareaApiService.obtenerTareasPorEspacio(1L);
        
        // Verificar resultados
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Tarea Espacio 1", resultado.get(0).getTitulo());
        assertEquals("MIRATE", resultado.get(0).getCategoria());
        assertEquals("Tarea Espacio 2", resultado.get(1).getTitulo());
        assertEquals("ORDENA", resultado.get(1).getCategoria());
    }
}