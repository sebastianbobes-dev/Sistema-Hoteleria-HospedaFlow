package com.hospedaflow.dto;

/**
 * DTO plano para registrar una habitacion o cambiar su estado.
 * "estado" es opcional: si no se envia, la habitacion nace DISPONIBLE.
 */
public class HabitacionRequestDTO {
    private String numero;
    private String tipo;
    private double precioBase;
    private int capacidad;
    private String estado;

    public HabitacionRequestDTO() {}

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
