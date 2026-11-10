package edu.uio.crmartesanal.usuario.dto;

/**
 * DTO (Data Transfer Object): representa el JSON que el cliente envía a
 * POST /api/v1/auth/login, por ejemplo {"username":"admin","password":"Admin123"}.
 *
 * Conexión: Spring (Jackson) convierte el cuerpo JSON en este record y
 * AuthController lo recibe con @RequestBody.
 *
 * SOLID (S): su única responsabilidad es transportar los datos de entrada.
 * Se separa del modelo Usuario para que lo que viaja por HTTP no quede
 * atado a la entidad interna del dominio.
 */
public record LoginRequest(String username, String password) { }
