package com.ordenconmimo.orden_con_mimo_frontend.models;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class TareaTest {

    @Test
    public void testConstructorAndGetters() {
        Long id = 1L;
        String titulo = "Test Tarea";
        String descripcion = "Descripción de prueba";
        String categoria = "MIRATE";
        LocalDate fechaLimite = LocalDate.now();
        boolean completada = false;

        Tarea tarea = new Tarea(id, titulo, descripcion, categoria, fechaLimite, completada);

        assertEquals(id, tarea.getId());
        assertEquals(titulo, tarea.getTitulo());
        assertEquals(descripcion, tarea.getDescripcion());
        assertEquals(categoria, tarea.getCategoria());
        assertEquals(fechaLimite, tarea.getFechaLimite());
        assertEquals(completada, tarea.isCompletada());
    }

    @Test
    public void testSetters() {
        Tarea tarea = new Tarea();
        Long id = 2L;
        String titulo = "Tarea Actualizada";
        String descripcion = "Nueva descripción";
        String categoria = "IMAGINA";
        LocalDate fechaLimite = LocalDate.now().plusDays(1);
        boolean completada = true;

        tarea.setId(id);
        tarea.setTitulo(titulo);
        tarea.setDescripcion(descripcion);
        tarea.setCategoria(categoria);
        tarea.setFechaLimite(fechaLimite);
        tarea.setCompletada(completada);

        assertEquals(id, tarea.getId());
        assertEquals(titulo, tarea.getTitulo());
        assertEquals(descripcion, tarea.getDescripcion());
        assertEquals(categoria, tarea.getCategoria());
        assertEquals(fechaLimite, tarea.getFechaLimite());
        assertEquals(completada, tarea.isCompletada());
    }

    @Test
    public void testDefaultConstructor() {
        Tarea tarea = new Tarea();

        assertNull(tarea.getId());
        assertNull(tarea.getTitulo());
        assertNull(tarea.getDescripcion());
        assertNull(tarea.getCategoria());
        assertNull(tarea.getFechaLimite());
        assertFalse(tarea.isCompletada());
    }
}