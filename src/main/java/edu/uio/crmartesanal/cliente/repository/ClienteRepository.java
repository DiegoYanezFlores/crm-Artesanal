package edu.uio.crmartesanal.cliente.repository;
import edu.uio.crmartesanal.cliente.model.Cliente;
import java.util.List;
import java.util.Optional;
/**
 * Contrato de acceso a datos del cliente (las 4 operaciones del CRUD + 2 consultas de apoyo).
 */
public interface ClienteRepository {
    List<Cliente> findAll();                                    // R: leer todos
    Optional<Cliente> findById(Long id);                        // R: leer uno
    boolean existsByCorreo(String correo);                      // apoyo: ¿ya existe ese correo?
    boolean existsByCorreoAndIdNot(String correo, Long id);     // apoyo: ¿lo tiene OTRO cliente?
    Cliente save(Cliente cliente);                              // C: insertar
    boolean update(Cliente cliente);                            // U: actualizar (¿existía?)
    boolean deleteById(Long id);                                // D: borrar (¿existía?)
}