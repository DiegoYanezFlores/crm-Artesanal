package edu.uio.crmartesanal.usuario.repository;

import edu.uio.crmartesanal.usuario.model.Usuario;

import java.util.Optional;

/**
 * Contrato de acceso a datos de usuarios: define QUÉ se puede pedir,
 * no CÓMO se guarda.
 *
 * Conexión: AuthService depende de esta interfaz; InMemoryUsuarioRepository
 * es la implementación actual, que UsuarioServletConfig expone como bean.
 *
 * SOLID:
 *  - D (Inversión de dependencias): el servicio depende de esta abstracción,
 *    no de una clase concreta.
 *  - O (Abierto/Cerrado): se puede agregar un JpaUsuarioRepository (base de
 *    datos) sin modificar AuthService ni AuthController.
 *  - I (Segregación de interfaces): solo expone el método que el login necesita.
 */
public interface UsuarioRepository {

    /**
     * Busca un usuario por su username.
     * Devuelve Optional vacío si no existe, así el llamador no maneja nulls.
     */
    Optional<Usuario> findByUsername(String username);
}
