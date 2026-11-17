package com.hospedaflow.Repositorio;

import java.util.List;
import java.util.Optional;

/**
 * Contrato genérico (Interfaz) para la capa de persistencia (Patrón Repository).
 * Permite desacoplar los servicios de la implementación concreta (JSON hoy, SQL/JPA en el futuro).
 *
 * @param <T>  Tipo de entidad gestionada
 * @param <ID> Tipo del identificador único
 */
public interface IRepositorio<T, ID> {
    List<T> obtenerTodas();
    Optional<T> buscarPorId(ID id);
    void guardar(T entidad);
    void eliminar(ID id);
}
