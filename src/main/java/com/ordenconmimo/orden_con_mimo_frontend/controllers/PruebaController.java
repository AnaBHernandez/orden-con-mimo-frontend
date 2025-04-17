package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/prueba")
public class PruebaController {

    private final TareaApiService tareaApiService;
    private final RestTemplate restTemplate;
    private final String apiUrl;

    @Autowired
    public PruebaController(TareaApiService tareaApiService, RestTemplate restTemplate) {
        this.tareaApiService = tareaApiService;
        this.restTemplate = restTemplate;
        this.apiUrl = "http://localhost:8082/api/tareas";
    }

    @GetMapping
    public String mostrarFormularioPrueba() {
        return "prueba";
    }

    @PostMapping("/crear")
    @ResponseBody
    public String crearTareaPrueba(@RequestParam String titulo,
            @RequestParam String descripcion,
            @RequestParam String categoria) {
        try {
            Map<String, Object> tareaData = new HashMap<>();
            tareaData.put("nombre", titulo);
            tareaData.put("descripcion", descripcion);
            tareaData.put("categoria", categoria);
            tareaData.put("completada", false);

            System.out.println("Enviando datos directamente: " + tareaData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(tareaData, headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Object.class);

            return "Respuesta: " + response.getStatusCode() + "<br>Cuerpo: " + response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage() + "<br><pre>" + e.toString() + "</pre>";
        }
    }

    @GetMapping("/prueba/listar")
    @ResponseBody
    public String listarTareasPrueba() {
        try {
            ResponseEntity<Object> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    Object.class);

            return "Respuesta: " + response.getStatusCode() + "<br>Cuerpo: " + response.getBody();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}