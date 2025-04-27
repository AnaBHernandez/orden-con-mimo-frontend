package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("all")
class SesionControllerTest {

    @Mock
    private HttpSession httpSession;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private SesionController sesionController;
    
    // Credenciales correctas para las pruebas
    private static final String USUARIO_CORRECTO = "admin";
    private static final String PASSWORD_CORRECTO = "password";

    @BeforeEach
    void setUp() {
        // Se pueden agregar configuraciones comunes para todas las pruebas
    }

    @Test
    @DisplayName("mostrarLogin debe retornar la vista login")
    void mostrarLogin_DeberiaRetornarVistaLogin() {
        // Ejecutar método a probar
        String viewName = sesionController.mostrarLogin();
        
        // Verificar resultados
        assertEquals("login", viewName, "El nombre de la vista debe ser 'login'");
    }

    @Test
    @DisplayName("procesarLogin con credenciales correctas debe redirigir a inicio")
    void procesarLogin_ConCredencialesCorrectas_DeberiaRedirecionarAInicio() {
        // Ejecutar método a probar
        String viewName = sesionController.procesarLogin(
                USUARIO_CORRECTO, 
                PASSWORD_CORRECTO, 
                httpSession, 
                redirectAttributes);
        
        // Verificar resultados
        verify(httpSession, times(1)).setAttribute(eq("usuarioLogueado"), eq(USUARIO_CORRECTO));
        assertEquals("redirect:/", viewName, "Debe redireccionar a la página de inicio");
    }

    @Test
    @DisplayName("procesarLogin con usuario incorrecto debe redirigir a login con error")
    void procesarLogin_ConUsuarioIncorrecto_DeberiaRedirecionarALogin() {
        String usuarioIncorrecto = "usuario_incorrecto";
        
        String viewName = sesionController.procesarLogin(
                usuarioIncorrecto, 
                PASSWORD_CORRECTO, 
                httpSession, 
                redirectAttributes);
        
        verify(httpSession, never()).setAttribute(anyString(), anyString());
        verify(redirectAttributes).addFlashAttribute(eq("error"), anyString());
        assertEquals("redirect:/login", viewName, "Debe redireccionar a la página de login");
    }
    
    @Test
    @DisplayName("procesarLogin con contraseña incorrecta debe redirigir a login con error")
    void procesarLogin_ConPasswordIncorrecto_DeberiaRedirecionarALogin() {
        String passwordIncorrecto = "password_incorrecto";
        
        String viewName = sesionController.procesarLogin(
                USUARIO_CORRECTO, 
                passwordIncorrecto, 
                httpSession, 
                redirectAttributes);
        
        verify(httpSession, never()).setAttribute(anyString(), anyString());
        verify(redirectAttributes).addFlashAttribute(eq("error"), anyString());
        assertEquals("redirect:/login", viewName, "Debe redireccionar a la página de login");
    }
    
    @Test
    @DisplayName("procesarLogin con credenciales nulas debe redirigir a login con error")
    void procesarLogin_ConCredencialesNulas_DeberiaRedirecionarALogin() {
        String viewName1 = sesionController.procesarLogin(
                null, 
                PASSWORD_CORRECTO, 
                httpSession, 
                redirectAttributes);
                
        reset(httpSession, redirectAttributes);
        String viewName2 = sesionController.procesarLogin(
                USUARIO_CORRECTO, 
                null, 
                httpSession, 
                redirectAttributes);
        
        verify(httpSession, never()).setAttribute(anyString(), anyString());
        verify(redirectAttributes, times(2)).addFlashAttribute(eq("error"), anyString());
        assertEquals("redirect:/login", viewName1, "Debe redireccionar a la página de login con usuario nulo");
        assertEquals("redirect:/login", viewName2, "Debe redireccionar a la página de login con contraseña nula");
    }

    @Test
    @DisplayName("logout debe invalidar la sesión y redirigir a login")
    void logout_DeberiaInvalidarSesionYRedirecionarALogin() {
        String viewName = sesionController.logout(httpSession);
        
        verify(httpSession, times(1)).invalidate();
        assertEquals("redirect:/login", viewName, "Debe redireccionar a la página de login");
    }
}