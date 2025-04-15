package com.ordenconmimo.orden_con_mimo_frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ordenconmimo"})
public class OrdenConMimoFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrdenConMimoFrontendApplication.class, args);
    }
}