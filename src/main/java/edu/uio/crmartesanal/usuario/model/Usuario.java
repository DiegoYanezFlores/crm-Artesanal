package edu.uio.crmartesanal.usuario.model;

/**
 * Modelo de dominio: un usuario del CRM (credenciales, nombre y rol).
 *
 * Conexión: UsuarioServletConfig crea las instancias iniciales, el repositorio
 * las guarda, AuthService las valida y AuthController las guarda en la sesión.
 *
 * SOLID (S): solo contiene datos del usuario, sin lógica de autenticación
 * ni de HTTP. Es inmutable (campos final y sin setters), lo que evita que
 * otra capa lo modifique por accidente.
 */
public class Usuario {
    private final String username;
    private final String password;
    private final String nombreCompleto;
    private final String rol;

    /** Constructor único: un Usuario siempre nace completo y válido. */
    public Usuario(String username, String password,
                   String nombreCompleto, String rol) {
        this.username = username;
        this.password = password;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    /** Identificador de login; el repositorio lo usa como clave de búsqueda. */
    public String getUsername() { return username; }

    /** Clave en texto plano (solo para fines académicos); AuthService la compara. */
    public String getPassword() { return password; }

    /** Nombre para mostrar; se devuelve en la respuesta del login. */
    public String getNombreCompleto() { return nombreCompleto; }

    /** Rol (ADMIN, VENDEDOR); sirve para autorizar acciones más adelante. */
    public String getRol() { return rol; }
}
