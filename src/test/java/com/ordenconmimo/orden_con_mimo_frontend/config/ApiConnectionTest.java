package com.ordenconmimo.orden_con_mimo_frontend.config;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

public class ApiConnectionTest {
    
    public static void main(String[] args) {
        try {
            RestTemplate testTemplate = new RestTemplate();
            String url = "http://localhost:8080/api/tareas";
            System.out.println("Intentando conectar a: " + url);
            ResponseEntity<String> response = testTemplate.getForEntity(url, String.class);
            System.out.println("Respuesta: " + response.getStatusCode());
            System.out.println("Cuerpo: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}