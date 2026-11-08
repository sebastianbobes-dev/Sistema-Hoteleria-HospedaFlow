package com.hospedaflow.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//codigo generado totalmente con ia.
@Component
public class JsonDataBase {

    private final ObjectMapper objectMapper;

    public JsonDataBase() {
        this.objectMapper = new ObjectMapper();
        // Registramos el módulo para soportar fechas modernas de Java (LocalDate)
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Lee una lista de objetos desde un archivo JSON de forma genérica.
     */
    public <T> List<T> leerListaDeJson(String rutaArchivo, TypeReference<List<T>> tipoRef) {
        try {
            File archivo = new File(rutaArchivo);
            if (!archivo.exists() || archivo.length() == 0) {
                return new ArrayList<>(); // Si no existe o está vacío, retorna lista vacía
            }
            return objectMapper.readValue(archivo, tipoRef);
        } catch (IOException e) {
            System.err.println("Error al leer el archivo JSON: " + rutaArchivo + " -> " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Guarda una lista completa de objetos en un archivo JSON de forma genérica.
     */
    public <T> void guardarLista(String rutaArchivo, List<T> lista) {
        try {
            File archivo = new File(rutaArchivo);
            
            // Asegurarse de que el directorio padre exista (ej. src/main/resources/data/)
            File directorioPadre = archivo.getParentFile();
            if (directorioPadre != null && !directorioPadre.exists()) {
                directorioPadre.mkdirs();
            }

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(archivo, lista);
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo JSON: " + rutaArchivo + " -> " + e.getMessage());
        }
    }
}
