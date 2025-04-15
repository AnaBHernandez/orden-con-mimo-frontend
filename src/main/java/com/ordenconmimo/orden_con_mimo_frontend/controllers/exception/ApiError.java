package com.ordenconmimo.orden_con_mimo_frontend.controllers.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class ApiError {

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleHttpClientErrorException(HttpClientErrorException ex, Model model) {
        int statusValue = ex.getStatusCode().value();
        String mensaje;
        
        switch (statusValue) {
            case 404:
                mensaje = "El recurso solicitado no se encontró en el servidor";
                break;
            case 401:
                mensaje = "No tienes autorización para acceder a este recurso";
                break;
            case 400:
                mensaje = "La solicitud enviada no es válida";
                break;
            default:
                mensaje = "Error en la comunicación con la API: " + ex.getMessage();
        }
        
        model.addAttribute("error", mensaje);
        model.addAttribute("status", statusValue);
        return "error";
    }
    
    @ExceptionHandler(ResourceAccessException.class)
    public String handleConnectionError(ResourceAccessException ex, Model model) {
        model.addAttribute("error", "No se pudo conectar con el servidor API. Asegúrate de que esté en ejecución.");
        model.addAttribute("status", 500);
        return "error";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("error", "Se produjo un error inesperado: " + ex.getMessage());
        model.addAttribute("status", 500);
        return "error";
    }
}