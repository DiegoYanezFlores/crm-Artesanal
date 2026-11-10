package edu.uio.crmartesanal.usuario.repository;

import edu.uio.crmartesanal.usuario.model.Usuario;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementación en memoria de UsuarioRepository.
 * Guarda los usuarios en un Map indexado por username.
 *
 * Conexión: no tiene anotaciones; UsuarioServletConfig la instancia y le
 * entrega la lista inicial de usuarios.
 *
 * SOLID:
 *  - S: solo se encarga de almacenar y buscar usuarios (no valida claves).
 *  - L (Liskov): puede reemplazar a cualquier otra implementación de
 *    UsuarioRepository sin que el resto del código note la diferencia.
 */
public class InMemoryUsuarioRepository implements UsuarioRepository {

    // Mapa username -> Usuario para búsqueda directa.
    private final Map<String, Usuario> usuarios;

    /**
     * Recibe los usuarios iniciales por constructor (inyección de dependencias)
     * en lugar de crearlos aquí, así los datos no quedan quemados en la clase.
     * Convierte la lista en un Map usando el username como clave.
     */
    public InMemoryUsuarioRepository(List<Usuario> iniciales) {
        this.usuarios = iniciales.stream()
                .collect(Collectors.toMap(Usuario::getUsername, Function.identity()));
    }

    /** Busca por clave en el Map; ofNullable convierte "no encontrado" en Optional vacío. */
    @Override
    public Optional<Usuario> findByUsername(String username) {
        return Optional.ofNullable(usuarios.get(username));
    }
}
