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
                
                // Procesar fechaLimite
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
        return guardarTarea(tarea);
    }

    public Tarea guardarTarea(Tarea tarea) {
        try {
            System.out.println("Guardando tarea en: " + apiUrl);

            Map<String, Object> tareaData = new HashMap<>();
            tareaData.put("nombre", tarea.getTitulo());
            tareaData.put("descripcion", tarea.getDescripcion());
            tareaData.put("categoria", tarea.getCategoria());
            tareaData.put("completada", false);

            System.out.println("Datos a enviar: " + tareaData);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(tareaData, headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Object.class);

            System.out.println("Respuesta recibida, status: " + response.getStatusCode());
            System.out.println("Cuerpo de respuesta: " + response.getBody());

            Tarea nuevaTarea = new Tarea();
            if (response.getBody() instanceof Map) {
                Map<String, Object> respMap = (Map<String, Object>) response.getBody();
                if (respMap.containsKey("id")) {
                    nuevaTarea.setId(Long.valueOf(respMap.get("id").toString()));
                }
                if (respMap.containsKey("nombre")) {
                    nuevaTarea.setTitulo(respMap.get("nombre").toString());
                }
                if (respMap.containsKey("descripcion")) {
                    nuevaTarea.setDescripcion(respMap.get("descripcion").toString());
                }
                if (respMap.containsKey("categoria")) {
                    nuevaTarea.setCategoria(respMap.get("categoria").toString());
                }
            }

            return nuevaTarea;
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
    
            Map<String, Object> tareaData = new HashMap<>();
            tareaData.put("nombre", tarea.getTitulo());  // Mapeo correcto: titulo -> nombre
            tareaData.put("descripcion", tarea.getDescripcion());
            tareaData.put("categoria", tarea.getCategoria());
            
            if (tarea.getFechaLimite() != null) {
                tareaData.put("fechaLimite", tarea.getFechaLimite().toString());
                System.out.println("Enviando fecha límite: " + tarea.getFechaLimite().toString());
            }
            
            tareaData.put("completada", tarea.isCompletada());
            System.out.println("Enviando completada: " + tarea.isCompletada());
    
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
    
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(tareaData, headers);
    
            System.out.println("Enviando datos para actualizar: " + tareaData);
    
            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    Object.class);
    
            System.out.println("Respuesta recibida, status: " + response.getStatusCode());
            System.out.println("Cuerpo de respuesta: " + response.getBody());
    
            Tarea tareaActualizada = new Tarea();
            if (response.getBody() instanceof Map) {
                Map<String, Object> respMap = (Map<String, Object>) response.getBody();
                if (respMap.containsKey("id")) {
                    tareaActualizada.setId(Long.valueOf(respMap.get("id").toString()));
                }
                if (respMap.containsKey("nombre")) {
                    tareaActualizada.setTitulo(respMap.get("nombre").toString());
                }
                if (respMap.containsKey("descripcion")) {
                    tareaActualizada.setDescripcion(respMap.get("descripcion").toString());
                }
                if (respMap.containsKey("categoria")) {
                    tareaActualizada.setCategoria(respMap.get("categoria").toString());
                }
                if (respMap.containsKey("completada")) {
                    tareaActualizada.setCompletada((Boolean) respMap.get("completada"));
                }
                // Procesar fechaLimite de la respuesta
                if (respMap.containsKey("fechaLimite") && respMap.get("fechaLimite") != null) {
                    String fechaStr = respMap.get("fechaLimite").toString();
                    if (fechaStr.contains("T")) {
                        fechaStr = fechaStr.split("T")[0]; // Extraer solo la parte de la fecha
                    }
                    try {
                        tareaActualizada.setFechaLimite(LocalDate.parse(fechaStr));
                    } catch (Exception e) {
                        System.err.println("Error al parsear fechaLimite: " + fechaStr);
                    }
                }
            }
    
            return tareaActualizada;
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