package com.hospedaflow.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hospedaflow.model.entidades.Usuario;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion del JSON que sale por la API REST.
 * Oculta la contrasena de todo Usuario en las respuestas HTTP (tambien cuando va
 * anidado, p. ej. el huesped dentro de una reserva).
 *
 * Solo afecta al ObjectMapper de Spring MVC. JsonDataBase usa su propio ObjectMapper,
 * asi que las contrasenas se siguen guardando en usuarios.json con normalidad.
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer ocultarContrasenas() {
        return builder -> builder.mixIn(Usuario.class, UsuarioSinContrasena.class);
    }

    /** Mix-in: solo anade @JsonIgnore a los getters de contrasena de Usuario. */
    abstract static class UsuarioSinContrasena {
        @JsonIgnore
        abstract String getContrasena();

        @JsonIgnore
        abstract String getPassword();
    }
}
