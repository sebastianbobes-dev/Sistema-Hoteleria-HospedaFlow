package com.hospedaflow.model.entidades;
import com.hospedaflow.model.enums.EstadoReserva;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reserva implements Identificable {
    private Long id;
    private String codigoReserva;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private EstadoReserva estado;
    private Huesped huesped;
    private Habitacion habitacionAsignada;
    private Oferta ofertaAplicada;
    private double precioTotal;

    public Reserva() {
        this.estado = EstadoReserva.PENDIENTE;
    }

    public Reserva(Long id, String codigoReserva, LocalDate fechaEntrada, LocalDate fechaSalida, EstadoReserva estado,
            Huesped huesped) {
        this.id = id;
        this.codigoReserva = codigoReserva;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estado = estado != null ? estado : EstadoReserva.PENDIENTE;
        this.huesped = huesped;
    }

    public void confirmarReserva() {
        this.estado = EstadoReserva.CONFIRMADA;
    }
    public void cancelarReserva() {
        this.estado = EstadoReserva.CANCELADA;
    }
    public void modificarReserva(LocalDate nuevaFechaEntrada, LocalDate nuevaFechaSalida) {
        this.fechaEntrada = nuevaFechaEntrada;
        this.fechaSalida = nuevaFechaSalida;
    }
    public void asignarHabitacion(Habitacion habitacion) {
        this.habitacionAsignada = habitacion;
    }

    /**
     * Comportamiento de dominio encapsulado en la entidad: calcula el total de noches.
     */
    public long calcularNoches() {
        if (this.fechaEntrada == null || this.fechaSalida == null) {
            return 0;
        }
        long dias = ChronoUnit.DAYS.between(this.fechaEntrada, this.fechaSalida);
        return Math.max(1L, dias);
    }

    /**
     * Comportamiento de dominio encapsulado: colabora con Habitacion y Oferta para calcular
     * la tarifa final aplicando el descuento si la oferta está vigente.
     */
    public double calcularPrecioFinal() {
        if (this.habitacionAsignada == null || this.fechaEntrada == null || this.fechaSalida == null) {
            return 0.0;
        }
        double subtotal = calcularNoches() * this.habitacionAsignada.getPrecio();
        if (this.ofertaAplicada != null) {
            subtotal = Math.max(0.0, subtotal - this.ofertaAplicada.calcularMontoDescuento(subtotal));
        }
        return subtotal;
    }

    // Getters y Setters
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }

    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    public Huesped getHuesped() {
        return huesped;
    }

    public void setHuesped(Huesped huesped) {
        this.huesped = huesped;
    }

    public Habitacion getHabitacionAsignada() {
        return habitacionAsignada;
    }

    public void setHabitacionAsignada(Habitacion habitacionAsignada) {
        this.habitacionAsignada = habitacionAsignada;
    }

    public Oferta getOfertaAplicada() {
        return ofertaAplicada;
    }

    public void setOfertaAplicada(Oferta ofertaAplicada) {
        this.ofertaAplicada = ofertaAplicada;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    
    
    
    
}
