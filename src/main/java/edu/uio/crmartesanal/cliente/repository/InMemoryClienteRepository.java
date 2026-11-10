package edu.uio.crmartesanal.cliente.repository;

import edu.uio.crmartesanal.cliente.model.Cliente;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Guarda los clientes en la memoria RAM mientras la aplicación está encendida.
 * Al reiniciar la aplicación, los datos se pierden (más adelante usaremos PostgreSQL).
 */
@Repository
public class InMemoryClienteRepository implements ClienteRepository {
    // "Tabla" en memoria: la llave es el id y el valor es el cliente.
    private final Map<Long, Cliente> clientes = new LinkedHashMap<>();
    // Contador que simula el id autoincremental de una base de datos.
    private long siguienteId = 1;

    @Override
    public synchronized List<Cliente> findAll() {
// Devolvemos una copia para que nadie modifique la "tabla" desde afuera.
        return new ArrayList<>(clientes.values());
    }

    @Override
    public synchronized boolean existsByCorreo(String correo) {
        return clientes.values()
                .stream()
                .anyMatch(cliente -> cliente.correo().equalsIgnoreCase(correo));
    }

    @Override
    public synchronized Cliente save(Cliente cliente) {
// El repositorio asigna el id; quien llama nunca lo decide.
        Cliente nuevoCliente = new Cliente(
                siguienteId++,
                cliente.nombre(),
                cliente.apellido(),
                cliente.correo(),
                cliente.telefono()
        );
        clientes.put(nuevoCliente.id(), nuevoCliente);
        return nuevoCliente;
    }
}