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
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }
    public synchronized Cliente registrarCliente(ClienteRequest request) {
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
        if (clienteRepository.existsByCorreo(correo)) {
            throw new ClienteDuplicadoException("Ya existe un cliente con ese correo");
        }
// id = null porque el repositorio es quien lo asigna.
        Cliente cliente = new Cliente(null, nombre, apellido, correo, telefono);
        return clienteRepository.save(cliente);
    }
    private String obligatorio(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
        return valor.trim();
    }
}