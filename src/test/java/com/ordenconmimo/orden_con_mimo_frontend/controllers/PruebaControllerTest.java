package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PruebaControllerTest {

    @Mock
    private TareaApiService tareaApiService;
    
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PruebaController pruebaController;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");
        
        mockMvc = MockMvcBuilders.standaloneSetup(pruebaController)
                .setViewResolvers(viewResolver)
                .build();
    }
    
    @Test
    public void testMostrarFormularioPrueba() throws Exception {
        mockMvc.perform(get("/prueba"))
               .andExpect(status().isOk())
               .andExpect(view().name("prueba"));
    }
    
    @Test
    public void testCrearTareaPrueba() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>("Tarea creada", HttpStatus.CREATED);
        
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenReturn(mockResponse);
        
        mockMvc.perform(post("/prueba/crear")
                .param("titulo", "Tarea de prueba")
                .param("descripcion", "Descripción de prueba")
                .param("categoria", "MIRATE"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Respuesta: 201")));
        
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class));
    }
    
    @Test
    public void testListarTareasPrueba() throws Exception {
        ResponseEntity<Object> mockResponse = new ResponseEntity<>("Lista de tareas", HttpStatus.OK);
        
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                isNull(),
                eq(Object.class)))
                .thenReturn(mockResponse);
        
        mockMvc.perform(get("/prueba/prueba/listar"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Respuesta: 200")));
        
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                isNull(),
                eq(Object.class));
    }
    
    @Test
    public void testCrearTareaPruebaConError() throws Exception {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)))
                .thenThrow(new RuntimeException("Error de conexión"));
        
        mockMvc.perform(post("/prueba/crear")
                .param("titulo", "Tarea de prueba")
                .param("descripcion", "Descripción de prueba")
                .param("categoria", "MIRATE"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error:")));
        
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class));
    }
    
    @Test
    public void testListarTareasPruebaConError() throws Exception {
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                isNull(),
                eq(Object.class)))
                .thenThrow(new RuntimeException("Error de conexión"));
        
        mockMvc.perform(get("/prueba/prueba/listar"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error:")));
        
        verify(restTemplate, times(1)).exchange(
                anyString(),
                eq(HttpMethod.GET),
                isNull(),
                eq(Object.class));
    }
}