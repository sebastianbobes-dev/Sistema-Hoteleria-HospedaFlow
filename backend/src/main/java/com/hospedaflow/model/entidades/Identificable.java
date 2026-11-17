package com.hospedaflow.model.entidades;

/**
 * Interfaz de dominio que define el contrato de identidad para todas las entidades
 * persistibles del sistema HospedaFlow.
 */
public interface Identificable {
    Long getId();
    void setId(Long id);
}
