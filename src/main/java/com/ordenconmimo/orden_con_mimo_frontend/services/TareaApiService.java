package com.ordenconmimo.orden_con_mimo_frontend.services;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

            ResponseEntity<Object> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    Object.class);

            System.out.println("Respuesta recibida, status: " + response.getStatusCode());
            System.out.println("Respuesta: " + response.getBody());

            List<Tarea> tareas = new ArrayList<>();

            if (response.getBody() instanceof List) {
                List<?> listaTareas = (List<?>) response.getBody();

                for (Object obj : listaTareas) {
                    if (obj instanceof Map) {
                        Map<String, Object> tareaMap = (Map<String, Object>) obj;
                        Tarea tarea = new Tarea();

                        if (tareaMap.containsKey("id")) {
                            tarea.setId(Long.valueOf(tareaMap.get("id").toString()));
                        }
                        if (tareaMap.containsKey("nombre")) {
                            tarea.setTitulo(tareaMap.get("nombre").toString());
                        }
                        if (tareaMap.containsKey("descripcion")) {
                            tarea.setDescripcion(tareaMap.get("descripcion").toString());
                        }
                        if (tareaMap.containsKey("categoria")) {
                            tarea.setCategoria(tareaMap.get("categoria").toString());
                        }

                        tareas.add(tarea);
                    }
                }
            }

            return tareas;
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

            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    Object.class);

            System.out.println("Respuesta recibida, status: " + response.getStatusCode());
            System.out.println("Cuerpo de respuesta COMPLETO: " + response.getBody());

            if (response.getBody() instanceof Map) {
                Map<String, Object> tareaMap = (Map<String, Object>) response.getBody();
                Tarea tarea = new Tarea();

                System.out.println("CAMPOS EN LA RESPUESTA:");
                for (String key : tareaMap.keySet()) {
                    System.out.println("Campo: " + key + " = " + tareaMap.get(key));
                }

                if (tareaMap.containsKey("id")) {
                    tarea.setId(Long.valueOf(tareaMap.get("id").toString()));
                }
                if (tareaMap.containsKey("nombre")) {
                    tarea.setTitulo(tareaMap.get("nombre").toString());
                }
                if (tareaMap.containsKey("descripcion")) {
                    tarea.setDescripcion(tareaMap.get("descripcion").toString());
                }
                if (tareaMap.containsKey("categoria")) {
                    tarea.setCategoria(tareaMap.get("categoria").toString());
                }
                if (tareaMap.containsKey("completada")) {
                    tarea.setCompletada((Boolean) tareaMap.get("completada"));
                    System.out.println("Valor de completada leído: " + tareaMap.get("completada"));
                }

                if (tareaMap.containsKey("fechaLimite") && tareaMap.get("fechaLimite") != null
                        && tareaMap.get("fechaLimite") != "") {

                    String fechaStr = tareaMap.get("fechaLimite").toString();
                    System.out.println("Fecha límite recibida del servidor: " + fechaStr);

                    try {
                        if (fechaStr.contains("T")) {
                            fechaStr = fechaStr.split("T")[0];
                        }
                        tarea.setFechaLimite(LocalDate.parse(fechaStr));
                        System.out.println("Fecha límite parseada: " + tarea.getFechaLimite());
                    } catch (Exception e) {
                        System.err.println("Error al parsear fechaLimite: " + fechaStr);
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("No se encontró fechaLimite en la respuesta o es null");
                }

                System.out.println("TAREA CREADA:");
                System.out.println(tarea);

                return tarea;
            }

            return null;
        } catch (Exception e) {
            System.err.println("Error al obtener tarea con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Tarea crearTarea(Tarea tarea) {
        try {
            String url = apiUrl; 
            
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("titulo", tarea.getTitulo());
            requestMap.put("descripcion", tarea.getDescripcion());
            requestMap.put("categoria", tarea.getCategoria());
            requestMap.put("completada", tarea.isCompletada());
       
            if (tarea.getFechaLimiteStr() != null && !tarea.getFechaLimiteStr().isEmpty()) {
                try {
                    LocalDate fecha = LocalDate.parse(tarea.getFechaLimiteStr());
                    tarea.setFechaLimite(fecha);
                    requestMap.put("fechaLimite", fecha.toString());
                    System.out.println("Enviando fecha: " + fecha.toString());
                } catch (Exception e) {
                    System.err.println("Error al convertir la fecha: " + e.getMessage());
                }
            } else if (tarea.getFechaLimite() != null) {
                requestMap.put("fechaLimite", tarea.getFechaLimite().toString());
            }
            
            System.out.println("Enviando datos para crear tarea: " + requestMap);
            
            ResponseEntity<Tarea> response = restTemplate.postForEntity(url, requestMap, Tarea.class);
            
            System.out.println("Respuesta del backend: " + response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error al crear tarea: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Tarea guardarTarea(Tarea tarea) {
        return crearTarea(tarea);
    }

    public Tarea actualizarTarea(Tarea tarea) {
        try {
            String url = apiUrl + "/" + tarea.getId();

            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("nombre", tarea.getTitulo());
            requestMap.put("descripcion", tarea.getDescripcion());
            requestMap.put("categoria", tarea.getCategoria());
            requestMap.put("completada", tarea.isCompletada());

            if (tarea.getFechaLimite() != null) {
                requestMap.put("fechaLimite", tarea.getFechaLimite().toString());
                System.out.println("Enviando fecha límite: " + tarea.getFechaLimite().toString());
            } else if (tarea.getFechaLimiteStr() != null && !tarea.getFechaLimiteStr().isEmpty()) {
                try {
                    LocalDate fecha = LocalDate.parse(tarea.getFechaLimiteStr());
                    requestMap.put("fechaLimite", fecha.toString());
                    System.out.println("Enviando fecha límite desde string: " + fecha);
                } catch (Exception e) {
                    System.err.println("Error al convertir fechaLimiteStr: " + e.getMessage());
                }
            }

            System.out.println("Actualizando tarea en: " + url);
            System.out.println("Enviando datos para actualizar: " + requestMap);

            restTemplate.put(url, requestMap);
            
            return obtenerTareaPorId(tarea.getId());
        } catch (Exception e) {
            System.err.println("Error al actualizar tarea: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean eliminarTarea(Long id) {
        try {
            String url = apiUrl + "/" + id;
            System.out.println("Eliminando tarea en: " + url);

            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<Void> response = restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    requestEntity,
                    Void.class);

            System.out.println("Respuesta recibida, status: " + response.getStatusCode());

            return response.getStatusCode().is2xxSuccessful();
        } catch (HttpClientErrorException e) {
            System.err.println("Error HTTP: " + e.getStatusCode());
            System.err.println("Respuesta: " + e.getResponseBodyAsString());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error al eliminar tarea: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}