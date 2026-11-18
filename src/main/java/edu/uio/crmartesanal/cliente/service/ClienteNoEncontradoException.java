package edu.uio.crmartesanal.cliente.service;
/**
 * Error de negocio: se pidió un cliente (por id) que no existe.
 */
public class ClienteNoEncontradoException extends RuntimeException {
    public ClienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}