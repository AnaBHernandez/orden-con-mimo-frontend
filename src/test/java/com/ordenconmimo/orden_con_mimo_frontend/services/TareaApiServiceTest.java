package com.ordenconmimo.orden_con_mimo_frontend.services;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TareaApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TareaApiService tareaApiService;

    private Tarea tareaEjemplo;
    private final String API_URL = "http://localhost:8080/api/tareas";

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
        // Arrange
        List<Tarea> tareas = Arrays.asList(
            tareaEjemplo,
            new Tarea(2L, "Test2", "Descripción2", "IMAGINA", LocalDate.now(), true)
        );
        
        ResponseEntity<List<Tarea>> responseEntity = new ResponseEntity<>(tareas, HttpStatus.OK);
        
        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(),
            any(ParameterizedTypeReference.class)
        )).thenReturn(responseEntity);
        
        List<Tarea> resultado = tareaApiService.obtenerTareas();
        
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
        
        verify(restTemplate).exchange(
            eq(API_URL),
            eq(HttpMethod.GET),
            isNull(),
            any(ParameterizedTypeReference.class)
        );
    }
    
    @Test
    public void testObtenerTareaPorId() {
        ResponseEntity<Tarea> responseEntity = new ResponseEntity<>(tareaEjemplo, HttpStatus.OK);
        
        when(restTemplate.getForEntity(eq(API_URL + "/1"), eq(Tarea.class)))
            .thenReturn(responseEntity);
        
        Tarea resultado = tareaApiService.obtenerTareaPorId(1L);
        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Test Tarea", resultado.getTitulo());
        
      
        verify(restTemplate).getForEntity(eq(API_URL + "/1"), eq(Tarea.class));
    }
    
    @Test
    public void testGuardarTarea() {
       
        Tarea nuevaTarea = new Tarea(null, "Nueva", "Descripción", "ORDENA", LocalDate.now(), false);
        Tarea tareaGuardada = new Tarea(3L, "Nueva", "Descripción", "ORDENA", LocalDate.now(), false);
        
        ResponseEntity<Tarea> responseEntity = new ResponseEntity<>(tareaGuardada, HttpStatus.CREATED);
        
        when(restTemplate.postForEntity(
            eq(API_URL),
            any(HttpEntity.class),
            eq(Tarea.class)
        )).thenReturn(responseEntity);
        
   
        Tarea resultado = tareaApiService.guardarTarea(nuevaTarea);
        
      
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertEquals("Nueva", resultado.getTitulo());
        
    
        verify(restTemplate).postForEntity(
            eq(API_URL),
            any(HttpEntity.class),
            eq(Tarea.class)
        );
    }
    
    @Test
    public void testActualizarTarea() {
      
        Tarea tareaActualizada = new Tarea(1L, "Actualizada", "Nueva descripción", "MUEVETE", LocalDate.now(), true);
        
        ResponseEntity<Tarea> responseEntity = new ResponseEntity<>(tareaActualizada, HttpStatus.OK);
        
        when(restTemplate.exchange(
            eq(API_URL + "/1"),
            eq(HttpMethod.PUT),
            any(HttpEntity.class),
            eq(Tarea.class)
        )).thenReturn(responseEntity);
        
   
        Tarea resultado = tareaApiService.actualizarTarea(tareaActualizada);
        
       
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Actualizada", resultado.getTitulo());
        assertEquals("MUEVETE", resultado.getCategoria());
        assertTrue(resultado.isCompletada());
        
      
        verify(restTemplate).exchange(
            eq(API_URL + "/1"),
            eq(HttpMethod.PUT),
            any(HttpEntity.class),
            eq(Tarea.class)
        );
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
        
        assertDoesNotThrow(() -> tareaApiService.eliminarTarea(1L));
        
        verify(restTemplate).exchange(
            eq(API_URL + "/1"),
            eq(HttpMethod.DELETE),
            isNull(),
            eq(Void.class)
        );
    }
    
    @Test
    public void testObtenerTareas_ErrorHandling() {
    
        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(),
            any(ParameterizedTypeReference.class)
        )).thenThrow(new RuntimeException("Error de conexión"));
        
    
        List<Tarea> resultado = tareaApiService.obtenerTareas();
        
      
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}