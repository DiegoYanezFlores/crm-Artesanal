package edu.uio.crmartesanal.usuario.controller;

import edu.uio.crmartesanal.usuario.dto.LoginRequest;
import edu.uio.crmartesanal.usuario.model.Usuario;
import edu.uio.crmartesanal.usuario.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * Capa de presentación (HTTP): recibe la petición, delega y arma la respuesta.
 * No contiene datos ni reglas de negocio.
 *
 * Flujo: cliente -> AuthController -> AuthService -> UsuarioRepository.
 * Todas las rutas empiezan con /api/v1/auth.
 *
 * SOLID:
 *  - S: su única razón de cambio es el formato de la API (códigos HTTP y JSON).
 *  - D: depende de AuthService, que Spring le inyecta; nunca hace "new".
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    /** Nombre del atributo de sesión donde se guarda el usuario autenticado. */
    public static final String USUARIO_SESION = "usuario";

    private final AuthService authService;

    /** Spring entrega el AuthService declarado como @Bean en UsuarioServletConfig. */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/v1/auth/login
     * - 400 si falta username o password (validación del formato de la petición).
     * - 401 si las credenciales no coinciden (decide AuthService).
     * - 200 si son correctas: guarda el usuario en la sesión HTTP y devuelve
     *   username, nombre y rol (nunca la clave).
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody LoginRequest datos,          // JSON -> LoginRequest
            HttpServletRequest request) {             // para acceder a la sesión
        if (datos.username() == null || datos.password() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Usuario y clave son obligatorios"));
        }
        // La regla de negocio se delega al servicio.
        Optional<Usuario> usuario = authService.autenticar(datos.username(), datos.password());
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales incorrectas"));
        }
        Usuario u = usuario.get();
        // Crea la sesión si no existe y marca al usuario como autenticado.
        request.getSession(true).setAttribute(USUARIO_SESION, u);
        return ResponseEntity.ok(Map.of(
                "username", u.getUsername(),
                "nombre", u.getNombreCompleto(),
                "rol", u.getRol()));
    }
}
