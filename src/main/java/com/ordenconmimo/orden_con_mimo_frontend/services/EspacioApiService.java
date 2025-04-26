package com.ordenconmimo.orden_con_mimo_frontend.services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ordenconmimo.orden_con_mimo_frontend.models.Espacio;
import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;

@Service
public class EspacioApiService {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public EspacioApiService(RestTemplate restTemplate, @Value("${api.base.url}") String apiBaseUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiBaseUrl + "/espacios";
    }

    public List<Espacio> obtenerEspacios() {
        try {
            System.out.println("Conectando a: " + apiUrl);
            ResponseEntity<Espacio[]> response = restTemplate.getForEntity(apiUrl, Espacio[].class);
            System.out.println("Respuesta recibida, status: " + response.getStatusCode());
    
            Espacio[] espacios = response.getBody();
            if (espacios != null) {
                System.out.println("Recibidos " + espacios.length + " espacios");
                for (Espacio e : espacios) {
                    System.out.println(" - Espacio: " + e.getId() + " - " + e.getNombre());
                }
            } else {
                System.out.println("El cuerpo de la respuesta es null");
            }
    
            return espacios != null ? Arrays.asList(espacios) : Collections.emptyList();
        } catch (Exception e) {
            System.out.println("Error al conectar: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Espacio obtenerEspacioPorId(Long id) {
        try {
            return restTemplate.getForObject(apiUrl + "/" + id, Espacio.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Espacio crearEspacio(Espacio espacio) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Espacio> requestEntity = new HttpEntity<>(espacio, headers);

            return restTemplate.postForObject(apiUrl, requestEntity, Espacio.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Espacio actualizarEspacio(Espacio espacio) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Espacio> requestEntity = new HttpEntity<>(espacio, headers);

            ResponseEntity<Espacio> response = restTemplate.exchange(
                    apiUrl + "/" + espacio.getId(),
                    HttpMethod.PUT,
                    requestEntity,
                    Espacio.class
            );

            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean eliminarEspacio(Long id) {
        try {
            restTemplate.delete(apiUrl + "/" + id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Tarea> obtenerTareasPorEspacio(Long espacioId) {
        try {
            ResponseEntity<Tarea[]> response = restTemplate.getForEntity(
                    apiUrl + "/" + espacioId + "/tareas",
                    Tarea[].class
            );

            Tarea[] tareas = response.getBody();
            return tareas != null ? Arrays.asList(tareas) : Collections.emptyList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
