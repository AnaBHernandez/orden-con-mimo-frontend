package com.ordenconmimo.orden_con_mimo_frontend.controllers.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApiErrorTest {

    @InjectMocks
    private ApiError apiError;

    @Mock
    private Model model;

    @Test
    public void testHandleHttpClientErrorException404() {

        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.NOT_FOUND, "Not Found", null, null,
                null);

        String result = apiError.handleHttpClientErrorException(ex, model);

        verify(model).addAttribute("error", "El recurso solicitado no se encontró en el servidor");
        verify(model).addAttribute("status", 404);
        assertEquals("error", result);
    }

    @Test
    public void testHandleHttpClientErrorException401() {

        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, "Unauthorized", null,
                null, null);

        String result = apiError.handleHttpClientErrorException(ex, model);

        verify(model).addAttribute("error", "No tienes autorización para acceder a este recurso");
        verify(model).addAttribute("status", 401);
        assertEquals("error", result);
    }

    @Test
    public void testHandleHttpClientErrorException400() {

        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.BAD_REQUEST, "Bad Request", null, null,
                null);

        String result = apiError.handleHttpClientErrorException(ex, model);

        verify(model).addAttribute("error", "La solicitud enviada no es válida");
        verify(model).addAttribute("status", 400);
        assertEquals("error", result);
    }

    @Test
    public void testHandleHttpClientErrorExceptionDefault() {

        HttpClientErrorException ex = HttpClientErrorException.create(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error",
                null, null, null);

        String result = apiError.handleHttpClientErrorException(ex, model);

        verify(model).addAttribute(eq("error"), contains("Error en la comunicación con la API"));
        verify(model).addAttribute("status", 500);
        assertEquals("error", result);
    }

    @Test
    public void testHandleConnectionError() {

        ResourceAccessException ex = new ResourceAccessException("Connection refused");

        String result = apiError.handleConnectionError(ex, model);

        verify(model).addAttribute("error",
                "No se pudo conectar con el servidor API. Asegúrate de que esté en ejecución.");
        verify(model).addAttribute("status", 500);
        assertEquals("error", result);
    }

    @Test
    public void testHandleGenericException() {
        Exception ex = new Exception("Unexpected error");

        String result = apiError.handleGenericException(ex, model);

        verify(model).addAttribute(eq("error"), contains("Se produjo un error inesperado"));
        verify(model).addAttribute("status", 500);
        assertEquals("error", result);
    }
}