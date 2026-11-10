package edu.uio.crmartesanal.usuario.config;

import edu.uio.crmartesanal.usuario.model.Usuario;
import edu.uio.crmartesanal.usuario.repository.InMemoryUsuarioRepository;
import edu.uio.crmartesanal.usuario.repository.UsuarioRepository;
import edu.uio.crmartesanal.usuario.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Composition root del módulo usuario: aquí se declaran y se conectan todos
 * los objetos (datos iniciales -> repositorio -> servicio).
 * Es el único lugar que conoce las clases concretas.
 *
 * Conexión: Spring detecta @Configuration al arrancar, ejecuta cada @Bean y
 * guarda el resultado en su contenedor. Luego inyecta AuthService en
 * AuthController por constructor.
 *
 * SOLID:
 *  - S: la construcción y el cableado de objetos se separa de la lógica de
 *    negocio y de HTTP.
 *  - D: las clases de negocio reciben abstracciones; esta clase decide cuál
 *    implementación entregar.
 *  - O: para pasar a base de datos solo se cambia el bean usuarioRepository.
 */
@Configuration
public class UsuarioServletConfig {

    /**
     * Bean del repositorio. Devuelve el tipo de la interfaz (UsuarioRepository)
     * para que quien lo use no dependa de la implementación en memoria.
     * Aquí se declaran los usuarios iniciales (antes estaban dentro del controlador).
     */
    @Bean
    public UsuarioRepository usuarioRepository() {
        return new InMemoryUsuarioRepository(List.of(
                new Usuario("admin", "Admin123", "Diego Yánez", "ADMIN"),
                new Usuario("vendedor", "Venta123", "Ana Guamán", "VENDEDOR")));
    }

    /**
     * Bean del servicio. Spring ve el parámetro UsuarioRepository y le pasa
     * automáticamente el bean de arriba (inyección de dependencias).
     */
    @Bean
    public AuthService authService(UsuarioRepository usuarioRepository) {
        return new AuthService(usuarioRepository);
    }
}
