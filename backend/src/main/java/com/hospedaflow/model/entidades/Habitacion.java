package com.hospedaflow.model.entidades;
import com.hospedaflow.model.enums.EstadoHabitacion;
public class Habitacion {
    private Long id;
    private String numero;
    private String tipo;
    private double precio;
    private int capacidad;
    private EstadoHabitacion estado;

    public Habitacion() {
        this.estado = EstadoHabitacion.DISPONIBLE;
    }

    public Habitacion(Long id, String numero, String tipo, double precio, int capacidad, EstadoHabitacion estado) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
        this.capacidad = capacidad;
        this.estado = estado;
    }
    public void cambiarEstado(EstadoHabitacion nuevoEstado) {
        this.estado = nuevoEstado;
    }
    
    public boolean estaDisponible() {
        return this.estado == EstadoHabitacion.DISPONIBLE;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }
    
}
