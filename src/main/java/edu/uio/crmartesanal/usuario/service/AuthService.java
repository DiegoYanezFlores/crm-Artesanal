package edu.uio.crmartesanal.usuario.service;

import edu.uio.crmartesanal.usuario.model.Usuario;
import edu.uio.crmartesanal.usuario.repository.UsuarioRepository;

import java.util.Optional;

/**
 * Capa de negocio: decide si unas credenciales son válidas.
 *
 * Conexión: AuthController la invoca; ella consulta a UsuarioRepository.
 * UsuarioServletConfig crea la instancia y le pasa el repositorio.
 *
 * SOLID:
 *  - S: solo contiene la regla de autenticación (no sabe de HTTP ni de
 *    cómo se almacenan los usuarios).
 *  - D: depende de la interfaz UsuarioRepository, no de la implementación.
 */
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    /** La dependencia llega por constructor: facilita cambiarla o probar con un repositorio falso. */
    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Autentica un usuario.
     * 1) Busca por username en el repositorio.
     * 2) Si existe, filtra comparando la clave.
     * Devuelve el Usuario si coincide, o Optional vacío si el usuario no
     * existe o la clave es incorrecta (sin revelar cuál de los dos falló).
     */
    public Optional<Usuario> autenticar(String username, String password) {
        return usuarioRepository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password));
    }
}
