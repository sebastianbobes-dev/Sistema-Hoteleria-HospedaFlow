package com.hospedaflow.model.entidades;
import java.time.LocalDate;

public class Oferta implements Identificable {
    private Long id;
    private String titulo;
    private String descripcion;
    private double descuento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activa;
    private Habitacion habitacionEnOferta;

    public Oferta(){
        this.activa = true;
    }

    public Oferta(Long id, String titulo, String descripcion, double descuento, LocalDate fechaInicio,
            LocalDate fechaFin, boolean activa, Habitacion habitacionEnOferta) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.descuento = descuento;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activa = activa;
        this.habitacionEnOferta = habitacionEnOferta;
    }

    public boolean esVigente(){
        LocalDate hoy = LocalDate.now();
        return activa && (fechaInicio != null && !hoy.isBefore(fechaInicio)) && (fechaFin != null && !hoy.isAfter(fechaFin));
    }

    /**
     * Comportamiento encapsulado de dominio: calcula el descuento aplicable sobre un subtotal.
     */
    public double calcularMontoDescuento(double subtotal) {
        if (!esVigente() || subtotal <= 0) {
            return 0.0;
        }
        double factor = this.descuento > 1.0 ? (this.descuento / 100.0) : this.descuento;
        return subtotal * factor;
    }

    //getter y setter
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public Habitacion getHabitacionEnOferta() {
        return habitacionEnOferta;
    }

    public void setHabitacionEnOferta(Habitacion habitacionEnOferta) {
        this.habitacionEnOferta = habitacionEnOferta;
    }

    //getter y setter
    

    
    
}
