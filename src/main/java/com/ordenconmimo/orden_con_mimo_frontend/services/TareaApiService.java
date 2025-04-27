package com.ordenconmimo.orden_con_mimo_frontend.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;

@Service
@SuppressWarnings("all")
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

                        if (tareaMap.containsKey("id") && tareaMap.get("id") != null) {
                            tarea.setId(Long.valueOf(tareaMap.get("id").toString()));
                        }

                        if (tareaMap.containsKey("nombre") && tareaMap.get("nombre") != null) {
                            tarea.setTitulo(tareaMap.get("nombre").toString());
                        } else if (tareaMap.containsKey("titulo") && tareaMap.get("titulo") != null) {
                            tarea.setTitulo(tareaMap.get("titulo").toString());
                        } else {
                            tarea.setTitulo("Sin título");
                        }

                        if (tareaMap.containsKey("descripcion") && tareaMap.get("descripcion") != null) {
                            tarea.setDescripcion(tareaMap.get("descripcion").toString());
                        }

                        if (tareaMap.containsKey("categoria") && tareaMap.get("categoria") != null) {
                            tarea.setCategoria(tareaMap.get("categoria").toString());
                        }

                        tareas.add(tarea);
                    }
                }
            }

            return tareas;
        } catch (HttpClientErrorException e) {
            System.err.println("Error HTTP: " + e.getStatusCode() + " - " + e.getMessage());
            return new ArrayList<>();
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error al obtener tareas: " + e.getMessage());
            System.err.println("Detalles del error: " + e.toString());
            return new ArrayList<>();
        }
    }

    public Tarea obtenerTareaPorId(Long id) {
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

                if (tareaMap.containsKey("id") && tareaMap.get("id") != null) {
                    tarea.setId(Long.valueOf(tareaMap.get("id").toString()));
                }
                if (tareaMap.containsKey("nombre") && tareaMap.get("nombre") != null) {
                    tarea.setTitulo(tareaMap.get("nombre").toString());
                }
                if (tareaMap.containsKey("descripcion") && tareaMap.get("descripcion") != null) {
                    tarea.setDescripcion(tareaMap.get("descripcion").toString());
                }
                if (tareaMap.containsKey("categoria") && tareaMap.get("categoria") != null) {
                    tarea.setCategoria(tareaMap.get("categoria").toString());
                }
                if (tareaMap.containsKey("completada") && tareaMap.get("completada") != null) {
                    tarea.setCompletada((Boolean) tareaMap.get("completada"));
                    System.out.println("Valor de completada leído: " + tareaMap.get("completada"));
                }

                if (tareaMap.containsKey("fechaLimite") && tareaMap.get("fechaLimite") != null
                        && !tareaMap.get("fechaLimite").toString().isEmpty()) {

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
                        System.err.println("Detalles del error: " + e.toString());
                    }
                } else {
                    System.out.println("No se encontró fechaLimite en la respuesta o es null");
                }

                System.out.println("TAREA CREADA:");
                System.out.println(tarea);

                return tarea;
            }

            return null;
        } catch (HttpClientErrorException e) {
            System.err.println("Error HTTP: " + e.getStatusCode() + " - " + e.getMessage());
            return null;
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Error al obtener tarea con ID " + id + ": " + e.getMessage());
            System.err.println("Detalles del error: " + e.toString());
            return null;
        }
    }

    public Tarea crearTarea(Tarea tarea) {
        try {
            System.out.println("=== CREANDO TAREA ===");
            System.out.println("Título original: " + tarea.getTitulo());
            
            System.out.println("URL completa para crear tarea: " + apiUrl);
    
            Map<String, Object> requestMap = new HashMap<>();
            if (tarea.getTitulo() != null) {
                requestMap.put("nombre", tarea.getTitulo());
            }
            if (tarea.getDescripcion() != null) {
                requestMap.put("descripcion", tarea.getDescripcion());
            }
            if (tarea.getCategoria() != null) {
                requestMap.put("categoria", tarea.getCategoria());
            }
            
            // Asegurar que completada no sea nulo
            requestMap.put("completada", tarea.isCompletada());
    
            if (tarea.getFechaLimite() != null) {
                requestMap.put("fechaLimite", tarea.getFechaLimite().toString());
                System.out.println("Fecha límite: " + tarea.getFechaLimite().toString());
            } else if (tarea.getFechaLimiteStr() != null && !tarea.getFechaLimiteStr().isEmpty()) {
                try {
                    LocalDate fecha = LocalDate.parse(tarea.getFechaLimiteStr());
                    requestMap.put("fechaLimite", fecha.toString());
                    System.out.println("Fecha límite (str): " + fecha.toString());
                } catch (Exception e) {
                    System.err.println("Error al convertir fechaLimiteStr: " + e.getMessage());
                }
            }
    
            System.out.println("Enviando al backend: " + requestMap);
    
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestMap, headers);
    
            ResponseEntity<Object> response = restTemplate.postForEntity(apiUrl, requestEntity, Object.class);
    
            System.out.println("Respuesta del backend: " + response.getStatusCode());
            System.out.println("Cuerpo de respuesta: " + response.getBody());
    
            if (response.getBody() instanceof Map) {
                Map<String, Object> responseMap = (Map<String, Object>) response.getBody();
                Tarea tareaCreada = new Tarea();
    
                if (responseMap != null) {
                    if (responseMap.containsKey("id") && responseMap.get("id") != null) {
                        tareaCreada.setId(Long.valueOf(responseMap.get("id").toString()));
                        System.out.println("ID creado: " + tareaCreada.getId());
                    }
    
                    if (responseMap.containsKey("nombre") && responseMap.get("nombre") != null) {
                        tareaCreada.setTitulo(responseMap.get("nombre").toString());
                        System.out.println("Título recibido: " + tareaCreada.getTitulo());
                    }
    
                    if (responseMap.containsKey("descripcion") && responseMap.get("descripcion") != null) {
                        tareaCreada.setDescripcion(responseMap.get("descripcion").toString());
                    }
    
                    if (responseMap.containsKey("categoria") && responseMap.get("categoria") != null) {
                        tareaCreada.setCategoria(responseMap.get("categoria").toString());
                    }
                    
                    if (responseMap.containsKey("completada") && responseMap.get("completada") != null) {
                        tareaCreada.setCompletada((Boolean) responseMap.get("completada"));
                    }
                }
    
                return tareaCreada;
            } else {
                System.err.println("Respuesta no es un Map: " + response.getBody());
            }
    
            return null;
        } catch (HttpClientErrorException e) {
            System.err.println("Error HTTP: " + e.getStatusCode() + " - " + e.getMessage());
            throw new RuntimeException("Error al crear la tarea: " + e.getMessage());
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            throw new RuntimeException("Error de conexión al crear la tarea: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al crear la tarea: " + e.getMessage());
            System.err.println("Detalles del error: " + e.toString());
            throw new RuntimeException("Error al crear la tarea: " + e.getMessage());
        }
    }

    public Tarea guardarTarea(Tarea tarea) {
        return crearTarea(tarea);
    }

    public Tarea actualizarTarea(Tarea tarea) {
        try {
            String url = apiUrl + "/" + tarea.getId();

            Map<String, Object> requestMap = new HashMap<>();
            if (tarea.getTitulo() != null) {
                requestMap.put("nombre", tarea.getTitulo());
            }
            if (tarea.getDescripcion() != null) {
                requestMap.put("descripcion", tarea.getDescripcion());
            }
            if (tarea.getCategoria() != null) {
                requestMap.put("categoria", tarea.getCategoria());
            }
            requestMap.put("completada", tarea.isCompletada());

            if (tarea.getFechaLimite() != null) {
                requestMap.put("fechaLimite", tarea.getFechaLimite().toString());
            } else if (tarea.getFechaLimiteStr() != null && !tarea.getFechaLimiteStr().isEmpty()) {
                try {
                    LocalDate fecha = LocalDate.parse(tarea.getFechaLimiteStr());
                    requestMap.put("fechaLimite", fecha.toString());
                } catch (Exception e) {
                    System.err.println("Error al convertir fechaLimiteStr: " + e.getMessage());
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestMap, headers);

            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.PUT,
                    requestEntity,
                    Object.class);

            if (response.getBody() instanceof Map) {
                Map<String, Object> responseMap = (Map<String, Object>) response.getBody();
                Tarea tareaActualizada = new Tarea();

                if (responseMap.containsKey("id") && responseMap.get("id") != null) {
                    tareaActualizada.setId(Long.valueOf(responseMap.get("id").toString()));
                }
                if (responseMap.containsKey("nombre") && responseMap.get("nombre") != null) {
                    tareaActualizada.setTitulo(responseMap.get("nombre").toString());
                }
                if (responseMap.containsKey("descripcion") && responseMap.get("descripcion") != null) {
                    tareaActualizada.setDescripcion(responseMap.get("descripcion").toString());
                }
                if (responseMap.containsKey("categoria") && responseMap.get("categoria") != null) {
                    tareaActualizada.setCategoria(responseMap.get("categoria").toString());
                }
                if (responseMap.containsKey("completada") && responseMap.get("completada") != null) {
                    tareaActualizada.setCompletada((Boolean) responseMap.get("completada"));
                }

                return tareaActualizada;
            }

            return null;
        } catch (HttpClientErrorException e) {
            System.err.println("Error HTTP: " + e.getStatusCode() + " - " + e.getMessage());
            throw new RuntimeException("Error al actualizar la tarea: " + e.getMessage());
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            throw new RuntimeException("Error de conexión al actualizar la tarea: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al actualizar la tarea: " + e.getMessage());
            System.err.println("Detalles del error: " + e.toString());
            throw new RuntimeException("Error al actualizar la tarea: " + e.getMessage());
        }
    }

    public Tarea actualizarTarea(Long id, Tarea tarea) {
        tarea.setId(id);
        return actualizarTarea(tarea);
    }

    public void eliminarTarea(Long id) {
        try {
            String url = apiUrl + "/" + id;
            restTemplate.exchange(
                    url,
                    HttpMethod.DELETE,
                    null,
                    Object.class);
        } catch (HttpClientErrorException e) {
            System.err.println("Error HTTP: " + e.getStatusCode() + " - " + e.getMessage());
        } catch (ResourceAccessException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al eliminar la tarea: " + e.getMessage());
            System.err.println("Detalles del error: " + e.toString());
        }
    }

    public List<Tarea> obtenerTareasPorCategoria(String categoria) {
        try {
            String url = apiUrl + "/categorias/" + categoria;
            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    Object.class);

            List<Tarea> tareas = new ArrayList<>();
            if (response.getBody() instanceof List) {
                List<?> tareasResponse = (List<?>) response.getBody();
                for (Object obj : tareasResponse) {
                    if (obj instanceof Map) {
                        Map<String, Object> tareaMap = (Map<String, Object>) obj;
                        Tarea tarea = new Tarea();
                        
                        if (tareaMap.containsKey("id") && tareaMap.get("id") != null) {
                            tarea.setId(Long.valueOf(tareaMap.get("id").toString()));
                        }
                        if (tareaMap.containsKey("nombre") && tareaMap.get("nombre") != null) {
                            tarea.setTitulo(tareaMap.get("nombre").toString());
                        }
                        if (tareaMap.containsKey("descripcion") && tareaMap.get("descripcion") != null) {
                            tarea.setDescripcion(tareaMap.get("descripcion").toString());
                        }
                        if (tareaMap.containsKey("categoria") && tareaMap.get("categoria") != null) {
                            tarea.setCategoria(tareaMap.get("categoria").toString());
                        }
                        
                        tareas.add(tarea);
                    }
                }
            }
            return tareas;
        } catch (Exception e) {
            System.err.println("Error al obtener tareas por categoría: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Tarea> obtenerTareasPorEspacio(Long espacioId) {
        try {
            String url = apiUrl + "/espacios/" + espacioId;
            ResponseEntity<Object> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    Object.class);

            List<Tarea> tareas = new ArrayList<>();
            if (response.getBody() instanceof List) {
                List<?> tareasResponse = (List<?>) response.getBody();
                for (Object obj : tareasResponse) {
                    if (obj instanceof Map) {
                        Map<String, Object> tareaMap = (Map<String, Object>) obj;
                        Tarea tarea = new Tarea();
                        
                        if (tareaMap.containsKey("id") && tareaMap.get("id") != null) {
                            tarea.setId(Long.valueOf(tareaMap.get("id").toString()));
                        }
                        if (tareaMap.containsKey("nombre") && tareaMap.get("nombre") != null) {
                            tarea.setTitulo(tareaMap.get("nombre").toString());
                        }
                        if (tareaMap.containsKey("descripcion") && tareaMap.get("descripcion") != null) {
                            tarea.setDescripcion(tareaMap.get("descripcion").toString());
                        }
                        if (tareaMap.containsKey("categoria") && tareaMap.get("categoria") != null) {
                            tarea.setCategoria(tareaMap.get("categoria").toString());
                        }
                        
                        tareas.add(tarea);
                    }
                }
            }
            return tareas;
        } catch (Exception e) {
            System.err.println("Error al obtener tareas por espacio: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}