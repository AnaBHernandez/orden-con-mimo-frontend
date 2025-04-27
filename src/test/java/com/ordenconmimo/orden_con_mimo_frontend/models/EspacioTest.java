package com.ordenconmimo.orden_con_mimo_frontend.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EspacioTest {

    private Espacio espacio;
    private static final Long ID = 1L;
    private static final String NOMBRE = "Espacio de Prueba";
    private static final String DESCRIPCION = "Descripción del espacio de prueba";
    private static final String TIPO = "TRABAJO";

    @BeforeEach
    void setUp() {
        espacio = new Espacio();
    }

    @Test
    void testConstructorVacio() {
        assertNotNull(espacio);
        assertNull(espacio.getId());
        assertNull(espacio.getNombre());
        assertNull(espacio.getDescripcion());
        assertNull(espacio.getTipo());
        assertNull(espacio.getTareas());
    }

    @Test
    void testConstructorConParametros() {
        espacio = new Espacio(ID, NOMBRE, DESCRIPCION, TIPO);
        
        assertNotNull(espacio);
        assertEquals(ID, espacio.getId());
        assertEquals(NOMBRE, espacio.getNombre());
        assertEquals(DESCRIPCION, espacio.getDescripcion());
        assertEquals(TIPO, espacio.getTipo());
        assertNull(espacio.getTareas());
    }

    @Test
    void testSetAndGetId() {
        espacio.setId(ID);
        assertEquals(ID, espacio.getId());
    }

    @Test
    void testSetAndGetNombre() {
        espacio.setNombre(NOMBRE);
        assertEquals(NOMBRE, espacio.getNombre());
    }

    @Test
    void testSetAndGetDescripcion() {
        espacio.setDescripcion(DESCRIPCION);
        assertEquals(DESCRIPCION, espacio.getDescripcion());
    }

    @Test
    void testSetAndGetTipo() {
        espacio.setTipo(TIPO);
        assertEquals(TIPO, espacio.getTipo());
    }

    @Test
    void testSetAndGetTareas() {
        List<Tarea> tareas = new ArrayList<>();
        
        Tarea tarea1 = new Tarea();
        tarea1.setId(1L);
        tarea1.setTitulo("Tarea 1");
        
        Tarea tarea2 = new Tarea();
        tarea2.setId(2L);
        tarea2.setTitulo("Tarea 2");
        
        tareas.add(tarea1);
        tareas.add(tarea2);
        
        espacio.setTareas(tareas);
        
        assertEquals(tareas, espacio.getTareas());
        assertEquals(2, espacio.getTareas().size());
        assertEquals("Tarea 1", espacio.getTareas().get(0).getTitulo());
        assertEquals("Tarea 2", espacio.getTareas().get(1).getTitulo());
    }

    @Test
    void testToString() {
        espacio = new Espacio(ID, NOMBRE, DESCRIPCION, TIPO);
        
        String expected = "Espacio{id=1, nombre='Espacio de Prueba', " +
                "descripcion='Descripción del espacio de prueba', tipo='TRABAJO'}";
        
        assertEquals(expected, espacio.toString());
    }
}