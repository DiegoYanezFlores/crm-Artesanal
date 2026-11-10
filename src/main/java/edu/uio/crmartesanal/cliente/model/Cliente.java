package edu.uio.crmartesanal.cliente.model;

public record Cliente(
        Long id,
        String nombre,
        String apellido,
        String correo,
        String telefono
) {
}
