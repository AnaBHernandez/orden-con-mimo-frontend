package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("all")
class MetodoMimoControllerTest {

    @Mock
    private Model model;

    @InjectMocks
    private MetodoMimoController metodoMimoController;

    @Test
    void mostrarMetodoMimo_DeberiaRetornarVistaSobreMimo() {
        String viewName = metodoMimoController.mostrarMetodoMimo(model);
        
        verify(model, times(1)).addAttribute(eq("title"), anyString());
        
        assertEquals("sobre-mimo", viewName);
    }
    
    @Test
    void mostrarCategoriasMimo_DeberiaRetornarVistaCategoriasMimo() {
        String viewName = metodoMimoController.mostrarCategoriasMimo(model);
        
        verify(model, times(1)).addAttribute(eq("title"), anyString());
        
        assertEquals("categorias-mimo", viewName);
    }
    
    @Test
    void mostrarBeneficiosMimo_DeberiaRetornarVistaBeneficiosMimo() {
        String viewName = metodoMimoController.mostrarBeneficiosMimo(model);
        
        verify(model, times(1)).addAttribute(eq("title"), anyString());
        
        assertEquals("beneficios-mimo", viewName);
    }
}