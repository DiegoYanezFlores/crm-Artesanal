package edu.uio.crmartesanal.cliente.service;

import edu.uio.crmartesanal.cliente.dto.ClienteRequest;
import edu.uio.crmartesanal.cliente.model.Cliente;
import edu.uio.crmartesanal.cliente.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Capa de negocio: aquí viven las reglas del CRM.
 * No sabe nada de HTTP, JSON ni Servlets.
 */

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    // Spring entrega (inyecta) la implementación del repositorio.
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // ---------- R: leer ----------
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }
    public Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException(
                        "No existe un cliente con id " + id));
    }

    // ---------- C: crear ----------
    public synchronized Cliente registrarCliente(ClienteRequest request) {
        Cliente cliente = validar(request);
        if (clienteRepository.existsByCorreo(cliente.correo())) {
            throw new ClienteDuplicadoException("Ya existe un cliente con ese correo");
        }
        return clienteRepository.save(cliente);
    }


    // ---------- U: actualizar ----------
    public synchronized Cliente actualizarCliente(Long id, ClienteRequest request) {
        obtenerCliente(id); // si no existe, lanza ClienteNoEncontradoException (404)
        Cliente datos = validar(request);
        if (clienteRepository.existsByCorreoAndIdNot(datos.correo(), id)) {
            throw new ClienteDuplicadoException("Ya existe otro cliente con ese correo");
        }

        Cliente actualizado = new Cliente(id, datos.nombre(), datos.apellido(),
                datos.correo(), datos.telefono());
        clienteRepository.update(actualizado);
        return actualizado;
    }


    // ---------- D: eliminar ----------
    public void eliminarCliente(Long id) {
        if (!clienteRepository.deleteById(id)) {
            throw new ClienteNoEncontradoException("No existe un cliente con id " + id);
        }
    }


    // Reglas de validación compartidas por crear y actualizar.
    // Devuelve un Cliente con id = null: el id lo decide la base de datos (crear)
    // o la URL (actualizar).
    private Cliente validar(ClienteRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Los datos del cliente son obligatorios");
        }
        String nombre = obligatorio(request.nombre(), "El nombre es obligatorio");
        String apellido = obligatorio(request.apellido(), "El apellido es obligatorio");
        String correo = obligatorio(request.correo(), "El correo es obligatorio")
                .toLowerCase();
        String telefono = request.telefono() == null ? "" : request.telefono().trim();
        if (!correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("El formato del correo no es válido");
        }
        return new Cliente(null, nombre, apellido, correo, telefono);
    }


    private String obligatorio(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }
}