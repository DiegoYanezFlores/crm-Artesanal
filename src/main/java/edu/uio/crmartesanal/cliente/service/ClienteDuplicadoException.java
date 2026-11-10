package edu.uio.crmartesanal.cliente.service;
/**
 * Error de negocio: se intentó registrar un correo que ya existe.
 */
public class ClienteDuplicadoException extends RuntimeException {
    public ClienteDuplicadoException(String mensaje) {
        super(mensaje);
    }
}