package edu.uio.crmartesanal.cliente.repository;

/**
 * Error técnico: falló la comunicación con la base de datos.
 * Envuelve a SQLException (que es "checked") para no contaminar las capas superiores.
 */
public class AccesoDatosException extends RuntimeException {
    public AccesoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
