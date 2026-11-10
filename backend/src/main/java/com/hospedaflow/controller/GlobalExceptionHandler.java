package com.hospedaflow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones de los servicios a codigos HTTP. Responde siempre con el
 * mensaje como texto plano, que es lo que lee el frontend (comun.js -> apiFetch).
 *  - IllegalArgumentException (dato invalido / no encontrado) -> 400 Bad Request
 *  - IllegalStateException (regla de negocio, p. ej. fechas ocupadas) -> 409 Conflict
 *  - JSON mal formado o fecha con formato incorrecto            -> 400 Bad Request
 *  - Cualquier otro error inesperado                             -> 500 (sin detalles internos)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> datoInvalido(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> conflicto(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> cuerpoInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .body("Solicitud mal formada. Revisa los datos enviados (las fechas deben ser yyyy-MM-dd).");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> errorInesperado(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor.");
    }
}
