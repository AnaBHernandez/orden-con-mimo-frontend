package com.ordenconmimo.orden_con_mimo_frontend.services;



import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TareaApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TareaApiService tareaApiService;

    private Tarea tareaEjemplo;
    private final String API_URL = "http://localhost:8082/api/tareas";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        
        // Configurar la propiedad de URL de API mediante reflexión
        try {
            java.lang.reflect.Field apiUrlField = TareaApiService.class.getDeclaredField("apiUrl");
            apiUrlField.setAccessible(true);
            apiUrlField.set(tareaApiService, API_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        tareaEjemplo = new Tarea(1L, "Test Tarea", "Descripción de prueba", "MIRATE", LocalDate.now(), false);
    }

    @Test
    public void testObtenerTareas() {
        // Prepare respuesta en formato Map para simular la estructura JSON
        List<Map<String, Object>> responseList = Arrays.asList(
            createTareaMap(1L, "Test Tarea", "Descripción de prueba", "MIRATE"),
            createTareaMap(2L, "Test2", "Descripción2", "IMAGINA")
        );
        
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseList, HttpStatus.OK);
        
        when(restTemplate.exchange(
            eq(API_URL),
            eq(HttpMethod.GET),
            isNull(),
            eq(Object.class)
        )).thenReturn(responseEntity);
        
        // Act
        List<Tarea> resultado = tareaApiService.obtenerTareas();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        
        verify(restTemplate).exchange(
            eq(API_URL),
            eq(HttpMethod.GET),
            isNull(),
            eq(Object.class)
        );
    }
    
    @Test
    public void testObtenerTareaPorId() {
        // Arrange
        long id = 1L;
        String url = API_URL + "/" + id;
        
        Map<String, Object> tareaMap = createTareaMap(1L, "Test Tarea", "Descripción de prueba", "MIRATE");
        tareaMap.put("completada", false);
        tareaMap.put("fechaCreacion", LocalDate.now().toString());
        tareaMap.put("fechaLimite", LocalDate.now().toString());
        
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(tareaMap, HttpStatus.OK);
        
        when(restTemplate.exchange(
            eq(url),
            eq(HttpMethod.GET),
            isNull(),
            eq(Object.class)
        )).thenReturn(responseEntity);
        
        // Act
        Tarea resultado = tareaApiService.obtenerTareaPorId(id);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Test Tarea", resultado.getTitulo());
        
        // Verify
        verify(restTemplate).exchange(
            eq(url),
            eq(HttpMethod.GET),
            isNull(),
            eq(Object.class)
        );
    }
    
    @Test
    public void testGuardarTarea() {
        // Arrange
        Tarea nuevaTarea = new Tarea(null, "Nueva", "Descripción", "ORDENA", LocalDate.now(), false);
        
        Map<String, Object> responseMap = createTareaMap(3L, "Nueva", "Descripción", "ORDENA");
        
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.CREATED);
        
        when(restTemplate.exchange(
            eq(API_URL),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(Object.class)
        )).thenReturn(responseEntity);
        
        // Act
        Tarea resultado = tareaApiService.guardarTarea(nuevaTarea);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Nueva", resultado.getTitulo());
    }
    
    @Test
    public void testActualizarTarea() {
        // Arrange
        Tarea tareaActualizada = new Tarea(1L, "Actualizada", "Nueva descripción", "MUEVETE", LocalDate.now(), true);
        
        Map<String, Object> responseMap = createTareaMap(1L, "Actualizada", "Nueva descripción", "MUEVETE");
        responseMap.put("completada", true);
        
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseMap, HttpStatus.OK);
        
        when(restTemplate.exchange(
            eq(API_URL + "/1"),
            eq(HttpMethod.PUT),
            any(HttpEntity.class),
            eq(Object.class)
        )).thenReturn(responseEntity);
        
        // Act
        Tarea resultado = tareaApiService.actualizarTarea(tareaActualizada);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Actualizada", resultado.getTitulo());
    }
    
    @Test
    public void testEliminarTarea() {
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        
        when(restTemplate.exchange(
            eq(API_URL + "/1"),
            eq(HttpMethod.DELETE),
            isNull(),
            eq(Void.class)
        )).thenReturn(responseEntity);
        
        boolean resultado = tareaApiService.eliminarTarea(1L);
        
        assertTrue(resultado);
    }
    
    @Test
    public void testObtenerTareas_ErrorHandling() {
        when(restTemplate.exchange(
            eq(API_URL),
            eq(HttpMethod.GET),
            isNull(),
            eq(Object.class)
        )).thenThrow(new RuntimeException("Error de conexión"));
        
        List<Tarea> resultado = tareaApiService.obtenerTareas();
        
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
    
    // Método de utilidad para crear mapas de tareas
    private Map<String, Object> createTareaMap(Long id, String nombre, String descripcion, String categoria) {
        Map<String, Object> tareaMap = new HashMap<>();
        tareaMap.put("id", id);
        tareaMap.put("nombre", nombre);
        tareaMap.put("descripcion", descripcion);
        tareaMap.put("categoria", categoria);
        return tareaMap;
    }
}