package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("all")
class HomeControllerTest {

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        // No se necesita configuración especial
    }

    @Test
    void home_DeberiaRetornarVistaHome() {
        // Ejecutar método a probar
        String viewName = homeController.home(model);
        
        // Verificar que se agrega atributo al modelo
        verify(model, times(1)).addAttribute(eq("title"), anyString());
        
        // Verificar que se retorna la vista correcta
        assertEquals("home", viewName);
    }
    
    @Test
    void irAPagina_DeberiaRetornarBienvenida() {
        String viewName = homeController.irAPagina(model);
        
        verify(model, times(1)).addAttribute(eq("title"), anyString());
        
        assertEquals("bienvenida", viewName);
    }
    
    @Test
    void dashboard_DeberiaRetornarVistaDashboard() {
        String viewName = homeController.dashboard(model);
        
        verify(model, times(1)).addAttribute(eq("title"), anyString());
        
        assertEquals("dashboard", viewName);
    }
    
    @Test
    void inicio_DeberiaRedirigeAHome() {
        String viewName = homeController.inicio();
        
        assertEquals("redirect:/", viewName);
    }
    
    @Test
    void salir_DeberiaRedirigirAHome() {
        String viewName = homeController.salir();
        
        assertEquals("redirect:/", viewName);
    }
}