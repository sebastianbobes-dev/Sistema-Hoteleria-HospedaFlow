package com.hospedaflow.dto;

import java.time.LocalDate;

/**
 * DTO plano para registrar una reserva o modificar sus fechas.
 * Las fechas viajan como texto ISO (yyyy-MM-dd); las relaciones viajan solo como ids.
 * Para modificar fechas basta con fechaEntrada y fechaSalida.
 */
public class ReservaRequestDTO {
    private String codigoReserva;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private Long huespedId;
    private Long habitacionId;
    private Long ofertaId;

    public ReservaRequestDTO() {}

    public String getCodigoReserva() { return codigoReserva; }
    public void setCodigoReserva(String codigoReserva) { this.codigoReserva = codigoReserva; }

    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }

    public Long getHuespedId() { return huespedId; }
    public void setHuespedId(Long huespedId) { this.huespedId = huespedId; }

    public Long getHabitacionId() { return habitacionId; }
    public void setHabitacionId(Long habitacionId) { this.habitacionId = habitacionId; }

    public Long getOfertaId() { return ofertaId; }
    public void setOfertaId(Long ofertaId) { this.ofertaId = ofertaId; }
}
