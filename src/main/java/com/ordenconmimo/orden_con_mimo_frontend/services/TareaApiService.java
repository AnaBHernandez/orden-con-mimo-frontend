package com.ordenconmimo.orden_con_mimo_frontend.services;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TareaApiService {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public TareaApiService(RestTemplate restTemplate, @Value("${api.base.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl + "/tareas";
    }

    public List<Tarea> obtenerTareas() {
        try {
            System.out.println("Conectando a: " + apiUrl);
            ResponseEntity<List<Tarea>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Tarea>>() {
                    });
            System.out.println("Respuesta recibida, status: " + response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al obtener tareas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public Tarea obtenerTareaPorId(long id) {
        try {
            String url = apiUrl + "/" + id;
            System.out.println("Conectando a: " + url);
            ResponseEntity<Tarea> response = restTemplate.getForEntity(url, Tarea.class);
            System.out.println("Respuesta recibida, status: " + response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al obtener tarea con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Tarea guardarTarea(Tarea tarea) {
        try {
            System.out.println("Guardando tarea en: " + apiUrl);
            System.out.println("Datos de la tarea: " + tarea);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Tarea> requestEntity = new HttpEntity<>(tarea, headers);

            ResponseEntity<Tarea> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Tarea.class);

            System.out.println("Respuesta recibida, status: " + response.getStatusCode());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            System.err.println("Error HTTP: " + e.getStatusCode());
            System.err.println("Respuesta: " + e.getResponseBodyAsString());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Error al guardar tarea: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Tarea actualizarTarea(Tarea tarea) {
        try {
            String url = apiUrl + "/" + tarea.getId();
            System.out.println("Actualizando tarea en: " + url);

            ResponseEntity<Tarea> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    new HttpEntity<>(tarea),
                    Tarea.class);

            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al actualizar tarea: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean eliminarTarea(long id) {
        try {
            String url = apiUrl + "/" + id;
            System.out.println("Eliminando tarea en: " + url);

            restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    null,
                    Void.class);

            return true;
        } catch (Exception e) {
            System.err.println("Error al eliminar tarea: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}