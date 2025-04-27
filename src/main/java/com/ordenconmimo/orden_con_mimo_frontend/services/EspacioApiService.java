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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.ordenconmimo.orden_con_mimo_frontend.models.Espacio;
import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;

@Service
@SuppressWarnings("all") // Silencia todas las advertencias en esta clase
public class EspacioApiService {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public EspacioApiService(RestTemplate restTemplate, @Value("${api.base.url}") String apiBaseUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiBaseUrl + "/espacios";
    }

    /**
     * Obtiene todos los espacios desde la API.
     * 
     * @return Lista de espacios, lista vacía si hay errores.
     */
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
                return Arrays.asList(espacios);
            } else {
                System.out.println("El cuerpo de la respuesta es null");
                return Collections.emptyList();
            }
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión al obtener espacios: " + e.getMessage());
            return Collections.emptyList();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error HTTP al obtener espacios: " + e.getStatusCode() + " - " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error inesperado al obtener espacios: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /**
     * Obtiene un espacio por su ID.
     * 
     * @param id ID del espacio a obtener
     * @return El espacio encontrado o null si no existe o hay error
     */
    public Espacio obtenerEspacioPorId(Long id) {
        try {
            System.out.println("Obteniendo espacio con ID: " + id);
            return restTemplate.getForObject(apiUrl + "/" + id, Espacio.class);
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión al obtener espacio: " + e.getMessage());
            return null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error HTTP al obtener espacio: " + e.getStatusCode() + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error inesperado al obtener espacio: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea un nuevo espacio.
     * 
     * @param espacio El espacio a crear
     * @return El espacio creado con su ID asignado, o null si hay error
     */
    public Espacio crearEspacio(Espacio espacio) {
        try {
            System.out.println("Creando nuevo espacio: " + espacio.getNombre());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Espacio> requestEntity = new HttpEntity<>(espacio, headers);

            Espacio espacioCreado = restTemplate.postForObject(apiUrl, requestEntity, Espacio.class);
            System.out.println("Espacio creado con ID: " + (espacioCreado != null ? espacioCreado.getId() : "null"));
            
            return espacioCreado;
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión al crear espacio: " + e.getMessage());
            return null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error HTTP al crear espacio: " + e.getStatusCode() + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error inesperado al crear espacio: " + e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza un espacio existente.
     * 
     * @param espacio El espacio con los datos actualizados
     * @return El espacio actualizado, o null si hay error
     */
    public Espacio actualizarEspacio(Espacio espacio) {
        try {
            System.out.println("Actualizando espacio con ID: " + espacio.getId());
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Espacio> requestEntity = new HttpEntity<>(espacio, headers);

            ResponseEntity<Espacio> response = restTemplate.exchange(
                    apiUrl + "/" + espacio.getId(),
                    HttpMethod.PUT,
                    requestEntity,
                    Espacio.class
            );

            Espacio espacioActualizado = response.getBody();
            System.out.println("Espacio actualizado: " + (espacioActualizado != null ? espacioActualizado.getNombre() : "null"));
            
            return espacioActualizado;
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión al actualizar espacio: " + e.getMessage());
            return null;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error HTTP al actualizar espacio: " + e.getStatusCode() + " - " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error inesperado al actualizar espacio: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina un espacio por su ID.
     * 
     * @param id ID del espacio a eliminar
     * @return true si se eliminó correctamente, false en caso de error
     */
    public boolean eliminarEspacio(Long id) {
        try {
            System.out.println("Eliminando espacio con ID: " + id);
            restTemplate.delete(apiUrl + "/" + id);
            System.out.println("Espacio eliminado con éxito");
            return true;
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión al eliminar espacio: " + e.getMessage());
            return false;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error HTTP al eliminar espacio: " + e.getStatusCode() + " - " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado al eliminar espacio: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todas las tareas asociadas a un espacio.
     * 
     * @param espacioId ID del espacio del que obtener las tareas
     * @return Lista de tareas, lista vacía si hay errores o no hay tareas
     */
    public List<Tarea> obtenerTareasPorEspacio(Long espacioId) {
        try {
            System.out.println("Obteniendo tareas del espacio con ID: " + espacioId);
            
            ResponseEntity<Tarea[]> response = restTemplate.getForEntity(
                    apiUrl + "/" + espacioId + "/tareas",
                    Tarea[].class
            );

            Tarea[] tareas = response.getBody();
            if (tareas != null) {
                System.out.println("Recibidas " + tareas.length + " tareas para el espacio " + espacioId);
                return Arrays.asList(tareas);
            } else {
                System.out.println("No se encontraron tareas para el espacio " + espacioId);
                return Collections.emptyList();
            }
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión al obtener tareas: " + e.getMessage());
            return Collections.emptyList();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error HTTP al obtener tareas: " + e.getStatusCode() + " - " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Error inesperado al obtener tareas: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}