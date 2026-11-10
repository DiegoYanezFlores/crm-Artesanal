package edu.uio.crmartesanal.cliente.repository;

import edu.uio.crmartesanal.cliente.model.Cliente;
import java.util.List;

public interface ClienteRepository {
    List<Cliente> findAll();
    boolean existsByCorreo(String correo);
    Cliente save(Cliente cliente);
}
