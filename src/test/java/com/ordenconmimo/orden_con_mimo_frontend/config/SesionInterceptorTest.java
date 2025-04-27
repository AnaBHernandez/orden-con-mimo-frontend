package com.ordenconmimo.orden_con_mimo_frontend.config;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("all") // Silencia todas las advertencias en esta clase
public class SesionInterceptorTest {

    @InjectMocks
    private SesionInterceptor sesionInterceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private Object handler;

    @BeforeEach
    void setUp() {
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void testPreHandleAccesoPaginaLogin() throws Exception {
        when(request.getRequestURI()).thenReturn("/login");

        boolean resultado = sesionInterceptor.preHandle(request, response, handler);

        assertTrue(resultado);
        verify(request, times(1)).getRequestURI();
        verify(request, never()).getSession();
    }

    @Test
    void testPreHandleAccesoRecursosEstaticos() throws Exception {
        when(request.getRequestURI()).thenReturn("/css/estilo.css");
        assertTrue(sesionInterceptor.preHandle(request, response, handler));

        when(request.getRequestURI()).thenReturn("/js/script.js");
        assertTrue(sesionInterceptor.preHandle(request, response, handler));

        when(request.getRequestURI()).thenReturn("/images/logo.png");
        assertTrue(sesionInterceptor.preHandle(request, response, handler));

        verify(request, times(3)).getRequestURI();
        verify(request, never()).getSession();
    }

    @Test
    void testPreHandleUsuarioNoLogueado() throws Exception {
        when(request.getRequestURI()).thenReturn("/tareas");
        when(session.getAttribute("usuarioLogueado")).thenReturn(null);

        boolean resultado = sesionInterceptor.preHandle(request, response, handler);

        assertFalse(resultado);
        verify(request, times(1)).getRequestURI();
        verify(request, times(1)).getSession();
        verify(session, times(1)).getAttribute("usuarioLogueado");
        verify(response, times(1)).sendRedirect("/login");
    }

    @Test
    void testPreHandleUsuarioLogueado() throws Exception {
        when(request.getRequestURI()).thenReturn("/tareas");
        when(session.getAttribute("usuarioLogueado")).thenReturn("admin");

        boolean resultado = sesionInterceptor.preHandle(request, response, handler);

        assertTrue(resultado);
        verify(request, times(1)).getRequestURI();
        verify(request, times(1)).getSession();
        verify(session, times(1)).getAttribute("usuarioLogueado");
        verify(response, never()).sendRedirect(anyString());
    }
}