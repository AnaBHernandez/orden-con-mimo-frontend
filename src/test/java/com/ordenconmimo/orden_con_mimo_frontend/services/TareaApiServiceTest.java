// src/test/java/com/ordenconmimo/orden_con_mimo_frontend/services/TareaApiServiceTest.java

package com.ordenconmimo.orden_con_mimo_frontend.services;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
        List<Map<String, Object>> responseList = Arrays.asList(
                createTareaMap(1L, "Test Tarea", "Descripción de prueba", "MIRATE"),
                createTareaMap(2L, "Test2", "Descripción2", "IMAGINA"));

        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseList, HttpStatus.OK);

        when(restTemplate.exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                isNull(),
                eq(Object.class))).thenReturn(responseEntity);

        List<Tarea> resultado = tareaApiService.obtenerTareas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(restTemplate).exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                isNull(),
                eq(Object.class));
    }

    @Test
    public void testObtenerTareaPorId() {
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
                eq(Object.class))).thenReturn(responseEntity);

        Tarea resultado = tareaApiService.obtenerTareaPorId(id);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Test Tarea", resultado.getTitulo());

        verify(restTemplate).exchange(
                eq(url),
                eq(HttpMethod.GET),
                isNull(),
                eq(Object.class));
    }

    @Test
    public void testGuardarTarea() {
        Tarea tarea = new Tarea();
        tarea.setTitulo("Test Tarea");
        tarea.setDescripcion("Descripción de prueba");
        tarea.setCategoria("MIRATE");

        Tarea tareaCreada = new Tarea();
        tareaCreada.setId(1L);
        tareaCreada.setTitulo("Test Tarea");

        ResponseEntity<Tarea> responseEntity = new ResponseEntity<>(tareaCreada, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(Tarea.class))).thenReturn(responseEntity);

        Tarea resultado = tareaApiService.crearTarea(tarea);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId().longValue());
        assertEquals("Test Tarea", resultado.getTitulo());

        verify(restTemplate).postForEntity(anyString(), any(), eq(Tarea.class));
    }


    @Test
    public void testActualizarTarea() {
        Tarea tarea = new Tarea();
        tarea.setId(1L);
        tarea.setTitulo("Test Tarea Actualizada");
        tarea.setDescripcion("Descripción actualizada");
        tarea.setCategoria("MIRATE");

        doNothing().when(restTemplate).put(anyString(), any());

        Tarea tareaActualizada = new Tarea();
        tareaActualizada.setId(1L);
        tareaActualizada.setTitulo("Test Tarea Actualizada");
        when(tareaApiService.obtenerTareaPorId(eq(1L))).thenReturn(tareaActualizada);

        Tarea resultado = tareaApiService.actualizarTarea(tarea);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Test Tarea Actualizada", resultado.getTitulo());

        verify(restTemplate).put(anyString(), any());
    }

    @Test
    public void testEliminarTarea() {
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(HttpStatus.NO_CONTENT);

        when(restTemplate.exchange(
                eq(API_URL + "/1"),
                eq(HttpMethod.DELETE),
                any(),
                eq(Void.class))).thenReturn(responseEntity);

        boolean resultado = tareaApiService.eliminarTarea(1L);

        assertTrue(resultado);
    }

    @Test
    public void testObtenerTareas_ErrorHandling() {
        when(restTemplate.exchange(
                eq(API_URL),
                eq(HttpMethod.GET),
                isNull(),
                eq(Object.class))).thenThrow(new RuntimeException("Error de conexión"));

        List<Tarea> resultado = tareaApiService.obtenerTareas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    private Map<String, Object> createTareaMap(Long id, String nombre, String descripcion, String categoria) {
        Map<String, Object> tareaMap = new HashMap<>();
        tareaMap.put("id", id);
        tareaMap.put("nombre", nombre);
        tareaMap.put("descripcion", descripcion);
        tareaMap.put("categoria", categoria);
        return tareaMap;
    }
}