package edu.uio.crmartesanal.cliente.dto;

public record ClienteRequest(
        String nombre,
        String apellido,
        String correo,
        String telefono
) {
}
