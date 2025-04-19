package com.ordenconmimo.orden_con_mimo_frontend.models;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tarea {
    private Long id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private LocalDate fechaLimite;
    private boolean completada;
    private String fechaLimiteStr;
    private LocalDate fechaCreacion;

    public Tarea() {
    }

    public Tarea(Long id, String titulo, String descripcion, String categoria, LocalDate fechaLimite,
            boolean completada) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.fechaLimite = fechaLimite;
        this.completada = completada;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombre() {
        return this.titulo;
    }

    public void setNombre(String nombre) {
        this.titulo = nombre;
    }

    public String getCategoriaMimo() {
        return this.categoria;
    }

    public void setCategoriaMimo(String categoriaMimo) {
        this.categoria = categoriaMimo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    public String getFechaLimiteStr() {
        return fechaLimiteStr;
    }

    public void setFechaLimiteStr(String fechaLimiteStr) {
        this.fechaLimiteStr = fechaLimiteStr;

        if (fechaLimiteStr != null && !fechaLimiteStr.isEmpty()) {
            try {
                this.fechaLimite = LocalDate.parse(fechaLimiteStr);
            } catch (Exception e) {
                System.err.println("No se pudo convertir la fecha: " + fechaLimiteStr);
            }
        }
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void actualizarFechaLimiteStr() {
        if (this.fechaLimite != null) {
            this.fechaLimiteStr = this.fechaLimite.toString();
        }
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", categoria='" + categoria + '\'' +
                ", fechaLimite=" + fechaLimite +
                ", fechaLimiteStr='" + fechaLimiteStr + '\'' +
                ", completada=" + completada +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}