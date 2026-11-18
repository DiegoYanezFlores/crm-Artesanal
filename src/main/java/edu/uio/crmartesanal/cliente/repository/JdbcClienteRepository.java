package edu.uio.crmartesanal.cliente.repository;

import edu.uio.crmartesanal.cliente.model.Cliente;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Guarda los clientes en PostgreSQL usando JDBC puro.
 * Reemplaza a InMemoryClienteRepository: el resto del sistema no se entera del cambio.
 */
@Repository
public class JdbcClienteRepository implements ClienteRepository {
    private static final String SELECT =
            "SELECT id, nombre, apellido, correo, telefono FROM cliente";

    // Spring entrega el DataSource configurado con application.properties.
    private final DataSource dataSource;
    public JdbcClienteRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    //GET ALL
    @Override
    public List<Cliente> findAll() {
        String sql = SELECT + " ORDER BY id";
        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Cliente> clientes = new ArrayList<>();
            while (rs.next()) {
                clientes.add(mapear(rs));
            }
            return clientes;
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo listar los clientes", e);
        }
    }

    //GET POR ID

    @Override
    public Optional<Cliente> findById(Long id) {
        String sql = SELECT + " WHERE id = ?";
        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo buscar el cliente " + id, e);
        }
    }

    // EXCEPCION DEL CORREO

    @Override
    public boolean existsByCorreo(String correo) {
        String sql = "SELECT EXISTS (SELECT 1 FROM cliente WHERE correo = ?)";
        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo verificar el correo", e);
        }
    }


    @Override
    public boolean existsByCorreoAndIdNot(String correo, Long id) {
        String sql = "SELECT EXISTS (SELECT 1 FROM cliente WHERE correo = ? AND id <> ?)";
        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setLong(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo verificar el correo", e);
        }
    }

    // PUT

    @Override
    public Cliente save(Cliente cliente) {
// RETURNING id: PostgreSQL devuelve el id que acaba de generar (BIGSERIAL).
        String sql = "INSERT INTO cliente (nombre, apellido, correo, telefono) "
                + "VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.nombre());
            ps.setString(2, cliente.apellido());
            ps.setString(3, cliente.correo());
            ps.setString(4, cliente.telefono());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return new Cliente(rs.getLong(1), cliente.nombre(), cliente.apellido(),
                        cliente.correo(), cliente.telefono());
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo guardar el cliente", e);
        }
    }

    // PUT
    @Override
    public boolean update(Cliente cliente) {
        String sql = "UPDATE cliente SET nombre = ?, apellido = ?, correo = ?, telefono = ? "
                + "WHERE id = ?";
        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.nombre());
            ps.setString(2, cliente.apellido());
            ps.setString(3, cliente.correo());
            ps.setString(4, cliente.telefono());
            ps.setLong(5, cliente.id());
            return ps.executeUpdate() > 0; // filas afectadas: 1 = existía, 0 = no existía
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo actualizar el cliente " + cliente.id(), e);
        }
    }

    // DELETE
    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection conexion = dataSource.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo eliminar el cliente " + id, e);
        }
    }


    // Convierte la fila actual del ResultSet en un objeto Cliente.
    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getLong("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("correo"),
                rs.getString("telefono")
        );
    }
}
