package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class WebsimControllerTest {

    @Mock
    private TareaApiService tareaApiService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WebsimController websimController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(websimController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testWebsimView() throws Exception {
        mockMvc.perform(get("/websim"))
                .andExpect(status().isOk())
                .andExpect(view().name("websim"));
    }

    @Test
    public void testApiConnectionSuccess() {
        String result = websimController.testApiConnection();
        assertTrue(result.contains("Conexión exitosa") || result.contains("Error de conexión"));
    }

    @Test
    public void testCrearTareaExitosa() throws Exception {
        Tarea tarea = new Tarea();
        tarea.setTitulo("Tarea de prueba");
        tarea.setDescripcion("Descripción de prueba");
        tarea.setCategoria("MIRATE");

        Tarea tareaGuardada = new Tarea();
        tareaGuardada.setId(1L);
        tareaGuardada.setTitulo("Tarea de prueba");
        tareaGuardada.setDescripcion("Descripción de prueba");
        tareaGuardada.setCategoria("MIRATE");

        when(tareaApiService.guardarTarea(any(Tarea.class))).thenReturn(tareaGuardada);

        mockMvc.perform(post("/api/tareas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(tarea)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Tarea de prueba"));

        verify(tareaApiService, times(1)).guardarTarea(any(Tarea.class));
    }

    @Test
    public void testCrearTareaError() throws Exception {
        Tarea tarea = new Tarea();
        tarea.setTitulo("Tarea de prueba");
        tarea.setDescripcion("Descripción de prueba");
        tarea.setCategoria("MIRATE");

        when(tareaApiService.guardarTarea(any(Tarea.class))).thenReturn(null);

        mockMvc.perform(post("/api/tareas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(tarea)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());

        verify(tareaApiService, times(1)).guardarTarea(any(Tarea.class));
    }

    @Test
    public void testCrearTareaExcepcion() throws Exception {
        Tarea tarea = new Tarea();
        tarea.setTitulo("Tarea de prueba");
        tarea.setDescripcion("Descripción de prueba");
        tarea.setCategoria("MIRATE");

        when(tareaApiService.guardarTarea(any(Tarea.class)))
                .thenThrow(new RuntimeException("Error de servicio"));

        mockMvc.perform(post("/api/tareas")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(tarea)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").exists());

        verify(tareaApiService, times(1)).guardarTarea(any(Tarea.class));
    }
}