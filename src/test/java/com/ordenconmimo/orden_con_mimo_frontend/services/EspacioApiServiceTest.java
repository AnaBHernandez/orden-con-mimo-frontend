package com.ordenconmimo.orden_con_mimo_frontend.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.ordenconmimo.orden_con_mimo_frontend.models.Espacio;
import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("all")
public class EspacioApiServiceTest {

    private static final String API_BASE_URL = "http://localhost:8082/api";
    private static final String API_ESPACIOS_URL = API_BASE_URL + "/espacios";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private EspacioApiService espacioApiService;

    private Espacio espacioMock;
    private Espacio[] espaciosMock;
    private Tarea[] tareasMock;

    @BeforeEach
    void setUp() {
        // Configurar la URL de la API usando ReflectionTestUtils
        ReflectionTestUtils.setField(espacioApiService, "apiUrl", API_ESPACIOS_URL);
        
        // Configurar datos de prueba
        espacioMock = new Espacio(1L, "Espacio de prueba", "Descripción de prueba", "TRABAJO");

        Espacio espacio2 = new Espacio(2L, "Espacio 2", "Descripción 2", "HOGAR");
        espaciosMock = new Espacio[] { espacioMock, espacio2 };

        // Configurar tareas de prueba
        Tarea tarea1 = new Tarea();
        tarea1.setId(1L);
        tarea1.setTitulo("Tarea 1");
        tarea1.setDescripcion("Descripción 1");
        tarea1.setCategoria("MIRATE");

        Tarea tarea2 = new Tarea();
        tarea2.setId(2L);
        tarea2.setTitulo("Tarea 2");
        tarea2.setDescripcion("Descripción 2");
        tarea2.setCategoria("ORDENA");

        tareasMock = new Tarea[] { tarea1, tarea2 };
    }

    @Test
    @DisplayName("obtenerEspacios debe retornar una lista de espacios cuando la API responde correctamente")
    void obtenerEspacios_DebeRetornarListaDeEspacios_CuandoApiRespondeCorrectamente() {
        // Configurar mock
        when(restTemplate.getForEntity(anyString(), eq(Espacio[].class)))
                .thenReturn(new ResponseEntity<>(espaciosMock, HttpStatus.OK));

        // Ejecutar método a probar
        List<Espacio> espacios = espacioApiService.obtenerEspacios();

        // Verificar resultados
        assertNotNull(espacios, "La lista de espacios no debe ser null");
        assertEquals(2, espacios.size(), "Debe retornar 2 espacios");
        assertEquals("Espacio de prueba", espacios.get(0).getNombre(), "El nombre del primer espacio debe coincidir");
        assertEquals("TRABAJO", espacios.get(0).getTipo(), "El tipo del primer espacio debe coincidir");
        assertEquals("Espacio 2", espacios.get(1).getNombre(), "El nombre del segundo espacio debe coincidir");
        assertEquals("HOGAR", espacios.get(1).getTipo(), "El tipo del segundo espacio debe coincidir");
    }

    @Test
    @DisplayName("obtenerEspacios debe retornar lista vacía cuando hay error de conexión")
    void obtenerEspacios_DebeRetornarListaVacia_CuandoHayErrorDeConexion() {
        // Configurar mock para simular error de conexión
        when(restTemplate.getForEntity(anyString(), eq(Espacio[].class)))
                .thenThrow(new ResourceAccessException("Error de conexión simulado"));

        // Ejecutar método a probar
        List<Espacio> espacios = espacioApiService.obtenerEspacios();

        // Verificar resultados
        assertNotNull(espacios, "La lista de espacios no debe ser null");
        assertTrue(espacios.isEmpty(), "La lista debe estar vacía cuando hay error de conexión");
    }

    @Test
    @DisplayName("obtenerEspacios debe retornar lista vacía cuando la API retorna respuesta vacía")
    void obtenerEspacios_DebeRetornarListaVacia_CuandoApiRetornaRespuestaVacia() {
        // Configurar mock para retornar cuerpo nulo
        when(restTemplate.getForEntity(anyString(), eq(Espacio[].class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // Ejecutar método a probar
        List<Espacio> espacios = espacioApiService.obtenerEspacios();

        // Verificar resultados
        assertNotNull(espacios, "La lista de espacios no debe ser null");
        assertTrue(espacios.isEmpty(), "La lista debe estar vacía cuando el cuerpo de la respuesta es nulo");
    }

    @Test
    @DisplayName("obtenerEspacioPorId debe retornar un espacio cuando existe")
    void obtenerEspacioPorId_DebeRetornarEspacio_CuandoExiste() {
        // Configurar mock
        when(restTemplate.getForObject(anyString(), eq(Espacio.class)))
                .thenReturn(espacioMock);

        // Ejecutar método a probar
        Espacio espacio = espacioApiService.obtenerEspacioPorId(1L);

        // Verificar resultados
        assertNotNull(espacio, "El espacio no debe ser null");
        assertEquals(1L, espacio.getId(), "El ID del espacio debe coincidir");
        assertEquals("Espacio de prueba", espacio.getNombre(), "El nombre del espacio debe coincidir");
        assertEquals("Descripción de prueba", espacio.getDescripcion(), "La descripción del espacio debe coincidir");
        assertEquals("TRABAJO", espacio.getTipo(), "El tipo del espacio debe coincidir");
        
        // Verificar que se llamó al método correcto
        verify(restTemplate).getForObject(contains("/1"), eq(Espacio.class));
    }

    @Test
    @DisplayName("obtenerEspacioPorId debe retornar null cuando el espacio no existe")
    void obtenerEspacioPorId_DebeRetornarNull_CuandoNoExiste() {
        // Configurar mock para simular espacio no encontrado
        when(restTemplate.getForObject(anyString(), eq(Espacio.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Ejecutar método a probar
        Espacio espacio = espacioApiService.obtenerEspacioPorId(999L);

        // Verificar resultados
        assertNull(espacio, "Debe retornar null cuando el espacio no existe");
        
        // Verificar que se llamó al método correcto
        verify(restTemplate).getForObject(contains("/999"), eq(Espacio.class));
    }

    @Test
    @DisplayName("crearEspacio debe retornar el espacio creado cuando la operación es exitosa")
    void crearEspacio_DebeRetornarEspacioCreado_CuandoOperacionExitosa() {
        // Configurar mock
        when(restTemplate.postForObject(anyString(), any(), eq(Espacio.class)))
                .thenReturn(espacioMock);

        // Crear espacio de prueba
        Espacio nuevoEspacio = new Espacio();
        nuevoEspacio.setNombre("Espacio de prueba");
        nuevoEspacio.setDescripcion("Descripción de prueba");
        nuevoEspacio.setTipo("TRABAJO");

        // Ejecutar método a probar
        Espacio espacioCreado = espacioApiService.crearEspacio(nuevoEspacio);

        // Verificar resultados
        assertNotNull(espacioCreado, "El espacio creado no debe ser null");
        assertEquals(1L, espacioCreado.getId(), "El ID del espacio debe coincidir");
        assertEquals("Espacio de prueba", espacioCreado.getNombre(), "El nombre del espacio debe coincidir");
        assertEquals("Descripción de prueba", espacioCreado.getDescripcion(), "La descripción del espacio debe coincidir");
        assertEquals("TRABAJO", espacioCreado.getTipo(), "El tipo del espacio debe coincidir");
        
        // Verificar que se llamó al método con el contenido correcto
        verify(restTemplate).postForObject(
            eq(API_ESPACIOS_URL), 
            argThat(entity -> {
                HttpEntity<Espacio> httpEntity = (HttpEntity<Espacio>) entity;
                Espacio body = httpEntity.getBody();
                return body.getNombre().equals("Espacio de prueba") &&
                       body.getDescripcion().equals("Descripción de prueba") &&
                       body.getTipo().equals("TRABAJO");
            }),
            eq(Espacio.class)
        );
    }

    @Test
    @DisplayName("crearEspacio debe retornar null cuando hay error de validación")
    void crearEspacio_DebeRetornarNull_CuandoHayErrorDeValidacion() {
        // Configurar mock para simular error de validación
        when(restTemplate.postForObject(anyString(), any(), eq(Espacio.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Crear espacio inválido (sin nombre)
        Espacio espacioInvalido = new Espacio();

        // Ejecutar método a probar
        Espacio espacioCreado = espacioApiService.crearEspacio(espacioInvalido);

        // Verificar resultados
        assertNull(espacioCreado, "Debe retornar null cuando hay error de validación");
    }

    @Test
    @DisplayName("actualizarEspacio debe retornar el espacio actualizado cuando la operación es exitosa")
    void actualizarEspacio_DebeRetornarEspacioActualizado_CuandoOperacionExitosa() {
        // Configurar mock
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Espacio.class)))
                .thenReturn(new ResponseEntity<>(espacioMock, HttpStatus.OK));

        // Crear espacio para actualizar
        Espacio espacioActualizar = new Espacio();
        espacioActualizar.setId(1L);
        espacioActualizar.setNombre("Espacio actualizado");
        espacioActualizar.setDescripcion("Descripción actualizada");
        espacioActualizar.setTipo("OCIO");

        // Ejecutar método a probar
        Espacio espacioActualizado = espacioApiService.actualizarEspacio(espacioActualizar);

        // Verificar resultados
        assertNotNull(espacioActualizado, "El espacio actualizado no debe ser null");
        assertEquals(1L, espacioActualizado.getId(), "El ID del espacio debe coincidir");
        // En este caso, el mock devuelve el espacio original, no el actualizado
        assertEquals("Espacio de prueba", espacioActualizado.getNombre(), "El nombre del espacio debe ser el del mock");
        
        // Verificar que se llamó al método con la URL y contenido correctos
        verify(restTemplate).exchange(
            contains("/1"),
            eq(HttpMethod.PUT),
            argThat(entity -> {
                HttpEntity<Espacio> httpEntity = (HttpEntity<Espacio>) entity;
                Espacio body = httpEntity.getBody();
                return body.getId() == 1L &&
                       body.getNombre().equals("Espacio actualizado") &&
                       body.getDescripcion().equals("Descripción actualizada") &&
                       body.getTipo().equals("OCIO");
            }),
            eq(Espacio.class)
        );
    }

    @Test
    @DisplayName("actualizarEspacio debe retornar null cuando el espacio no existe")
    void actualizarEspacio_DebeRetornarNull_CuandoEspacioNoExiste() {
        // Configurar mock para simular espacio no encontrado
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Espacio.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Crear espacio inexistente
        Espacio espacioInexistente = new Espacio();
        espacioInexistente.setId(999L);
        espacioInexistente.setNombre("Espacio inexistente");

        // Ejecutar método a probar
        Espacio espacioActualizado = espacioApiService.actualizarEspacio(espacioInexistente);

        // Verificar resultados
        assertNull(espacioActualizado, "Debe retornar null cuando el espacio no existe");
        
        // Verificar que se llamó al método con la URL correcta
        verify(restTemplate).exchange(
            contains("/999"),
            eq(HttpMethod.PUT),
            any(),
            eq(Espacio.class)
        );
    }

    @Test
    @DisplayName("eliminarEspacio debe retornar true cuando la eliminación es exitosa")
    void eliminarEspacio_DebeRetornarTrue_CuandoEliminacionExitosa() {
        // No es necesario configurar el mock para el caso exitoso
        // ya que el método delete() no retorna nada

        // Ejecutar método a probar
        boolean resultado = espacioApiService.eliminarEspacio(1L);

        // Verificar resultados
        assertTrue(resultado, "Debe retornar true cuando la eliminación es exitosa");
        
        // Verificar que se llamó al método con la URL correcta
        verify(restTemplate).delete(contains("/1"));
    }

    @Test
    @DisplayName("eliminarEspacio debe retornar false cuando el espacio no existe")
    void eliminarEspacio_DebeRetornarFalse_CuandoEspacioNoExiste() {
        // Configurar mock para simular espacio no encontrado
        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
            .when(restTemplate).delete(anyString());

        // Ejecutar método a probar
        boolean resultado = espacioApiService.eliminarEspacio(999L);

        // Verificar resultados
        assertFalse(resultado, "Debe retornar false cuando el espacio no existe");
        
        // Verificar que se llamó al método con la URL correcta
        verify(restTemplate).delete(contains("/999"));
    }

    @Test
    @DisplayName("eliminarEspacio debe retornar false cuando hay error de servidor")
    void eliminarEspacio_DebeRetornarFalse_CuandoHayErrorDeServidor() {
        // Configurar mock para simular error de servidor
        doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR))
            .when(restTemplate).delete(anyString());

        // Ejecutar método a probar
        boolean resultado = espacioApiService.eliminarEspacio(1L);

        // Verificar resultados
        assertFalse(resultado, "Debe retornar false cuando hay error de servidor");
    }

    @Test
    @DisplayName("obtenerTareasPorEspacio debe retornar una lista de tareas cuando la API responde correctamente")
    void obtenerTareasPorEspacio_DebeRetornarListaDeTareas_CuandoApiRespondeCorrectamente() {
        // Configurar mock
        when(restTemplate.getForEntity(anyString(), eq(Tarea[].class)))
                .thenReturn(new ResponseEntity<>(tareasMock, HttpStatus.OK));

        // Ejecutar método a probar
        List<Tarea> tareas = espacioApiService.obtenerTareasPorEspacio(1L);

        // Verificar resultados
        assertNotNull(tareas, "La lista de tareas no debe ser null");
        assertEquals(2, tareas.size(), "Debe retornar 2 tareas");
        assertEquals("Tarea 1", tareas.get(0).getTitulo(), "El título de la primera tarea debe coincidir");
        assertEquals("MIRATE", tareas.get(0).getCategoria(), "La categoría de la primera tarea debe coincidir");
        assertEquals("Tarea 2", tareas.get(1).getTitulo(), "El título de la segunda tarea debe coincidir");
        assertEquals("ORDENA", tareas.get(1).getCategoria(), "La categoría de la segunda tarea debe coincidir");
        
        // Verificar que se llamó al método con la URL correcta
        verify(restTemplate).getForEntity(contains("/1/tareas"), eq(Tarea[].class));
    }

    @Test
    @DisplayName("obtenerTareasPorEspacio debe retornar lista vacía cuando hay error de conexión")
    void obtenerTareasPorEspacio_DebeRetornarListaVacia_CuandoHayErrorDeConexion() {
        // Configurar mock para simular error de conexión
        when(restTemplate.getForEntity(anyString(), eq(Tarea[].class)))
                .thenThrow(new ResourceAccessException("Error de conexión simulado"));

        // Ejecutar método a probar
        List<Tarea> tareas = espacioApiService.obtenerTareasPorEspacio(1L);

        // Verificar resultados
        assertNotNull(tareas, "La lista de tareas no debe ser null");
        assertTrue(tareas.isEmpty(), "La lista debe estar vacía cuando hay error de conexión");
    }

    @Test
    @DisplayName("obtenerTareasPorEspacio debe retornar lista vacía cuando el espacio no tiene tareas")
    void obtenerTareasPorEspacio_DebeRetornarListaVacia_CuandoEspacioNoTieneTareas() {
        // Configurar mock para retornar cuerpo nulo
        when(restTemplate.getForEntity(anyString(), eq(Tarea[].class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // Ejecutar método a probar
        List<Tarea> tareas = espacioApiService.obtenerTareasPorEspacio(1L);

        // Verificar resultados
        assertNotNull(tareas, "La lista de tareas no debe ser null");
        assertTrue(tareas.isEmpty(), "La lista debe estar vacía cuando el espacio no tiene tareas");
    }
}