package com.ordenconmimo.orden_con_mimo_frontend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import com.ordenconmimo.orden_con_mimo_frontend.models.Tarea;
import com.ordenconmimo.orden_con_mimo_frontend.services.TareaApiService;

@Controller
public class WebsimController {

    @GetMapping("/websim")
    public String websimView() {
        return "websim";
    }

    public String testApiConnection() {
        try {
            ResponseEntity<String> response = new RestTemplate().getForEntity(
                    "http://localhost:8082/api/health", String.class);

            return "Conexión exitosa. Respuesta: " + response.getStatusCode();
        } catch (Exception e) {
            return "Error de conexión: " + e.getMessage();
        }
    }

    @GetMapping("/test-api")
    @ResponseBody
    public String testApiEndpoint() {
        return testApiConnection();
    }

    @PostMapping("/api/tareas")
    @ResponseBody
    public ResponseEntity<?> crearTarea(@RequestBody Tarea tarea) {
        try {
            System.out.println("Recibida solicitud para crear tarea: " + tarea.getTitulo());
            System.out.println("Datos de la tarea: " + tarea);

            Tarea nuevaTarea = tareaApiService.guardarTarea(tarea);
            if (nuevaTarea != null) {
                System.out.println("Tarea creada con éxito: " + nuevaTarea.getId());
                return ResponseEntity.ok(nuevaTarea);
            } else {
                System.err.println("Error: La respuesta del servicio es nula");
                return ResponseEntity.status(500)
                        .body(Map.of("error", "No se pudo crear la tarea. La respuesta del servicio fue nula."));
            }
        } catch (Exception e) {
            System.err.println("Error al crear tarea: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Error al crear tarea: " + e.getMessage()));
        }
    }

    @Autowired
    private TareaApiService tareaApiService;
}
