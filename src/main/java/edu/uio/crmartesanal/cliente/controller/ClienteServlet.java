package edu.uio.crmartesanal.cliente.controller;
import edu.uio.crmartesanal.cliente.dto.ClienteRequest;
import edu.uio.crmartesanal.cliente.model.Cliente;
import edu.uio.crmartesanal.cliente.service.ClienteDuplicadoException;
import edu.uio.crmartesanal.cliente.service.ClienteNoEncontradoException;
import edu.uio.crmartesanal.cliente.service.ClienteService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Capa de presentación con un Servlet "clásico": CRUD completo de clientes.
 * Atiende /api/clientes y /api/clientes/{id} (se registra en ClienteServletConfig).
 *
 * GET /api/clientes            -> listar todos (200)
 * GET /api/clientes/{id}       -> consultar uno (200 / 404)
 * POST /api/clientes           -> crear (201 / 400 / 409 / 415)
 * PUT /api/clientes/{id}       -> actualizar (200 / 400 / 404 / 409 / 415)
 * DELETE /api/clientes/{id}    -> eliminar (204 / 404)
 */
public class ClienteServlet extends HttpServlet {
    private final ClienteService clienteService;
    private final JsonMapper jsonMapper;

    public ClienteServlet(ClienteService clienteService, JsonMapper jsonMapper) {
        this.clienteService = clienteService;
        this.jsonMapper = jsonMapper;
    }

    // ==========================================================
// GET /api/clientes -> consultar todos los clientes
// ==========================================================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        List<Cliente> clientes = clienteService.listarClientes();
// Cabecera personalizada: cuántos clientes devolvemos.
        response.setHeader("X-Total-Clientes", String.valueOf(clientes.size()));
        escribirJson(response, HttpServletResponse.SC_OK, clientes); // 200
    }

    // ==========================================================
// POST /api/clientes -> registrar un cliente nuevo
// ==========================================================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
// 1. Revisar la cabecera Content-Type: solo aceptamos JSON.
        String contentType = request.getContentType();
        if (contentType == null || !contentType.startsWith("application/json")) {
            escribirError(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, // 415
                    "Envíe el cuerpo en JSON (Content-Type: application/json)");
            return;
        }
        try {
// 2. Leer el cuerpo (body) y convertir el JSON en un objeto Java.
            ClienteRequest clienteRequest =
                    jsonMapper.readValue(request.getInputStream(), ClienteRequest.class);
// 3. Delegar la regla de negocio al servicio.
            Cliente cliente = clienteService.registrarCliente(clienteRequest);
// 4. Responder 201 Created con el cliente ya registrado.
            escribirJson(response, HttpServletResponse.SC_CREATED, cliente);
        } catch (JacksonException e) {
            escribirError(response, HttpServletResponse.SC_BAD_REQUEST, // 400
                    "El JSON enviado no es válido");
        } catch (ClienteDuplicadoException e) {
            escribirError(response, HttpServletResponse.SC_CONFLICT, // 409
                    e.getMessage());
        } catch (IllegalArgumentException e) {
            escribirError(response, HttpServletResponse.SC_BAD_REQUEST, // 400
                    e.getMessage());
        }
    }

    // ==========================================================
// PUT -> actualizar el cliente {id}
// ==========================================================
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Long id = leerId(request);
            if (id == null) {
                escribirError(response, HttpServletResponse.SC_BAD_REQUEST, // 400
                        "Indique el id en la URL: PUT /api/clientes/{id}");
                return;
            }
            if (!esJson(request)) {
                escribirError(response, HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, // 415
                        "Envíe el cuerpo en JSON (Content-Type: application/json)");
                return;
            }
            ClienteRequest datos =
                    jsonMapper.readValue(request.getInputStream(), ClienteRequest.class);
            Cliente actualizado = clienteService.actualizarCliente(id, datos);
            escribirJson(response, HttpServletResponse.SC_OK, actualizado); // 200
        } catch (RuntimeException e) {
            responderError(response, e);
        }
    }

    // ==========================================================
    // DELETE -> eliminar el cliente {id}
    // ==========================================================
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            Long id = leerId(request);
            if (id == null) {
                escribirError(response, HttpServletResponse.SC_BAD_REQUEST, // 400
                        "Indique el id en la URL: DELETE /api/clientes/{id}");
                return;
            }
            clienteService.eliminarCliente(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204: éxito sin cuerpo
        } catch (RuntimeException e) {
            responderError(response, e);
        }
    }

    // ==========================================================
// Métodos de apoyo
// ==========================================================
// Extrae el id de la URL. /api/clientes -> null ; /api/clientes/5 -> 5.
// Si lo que sigue no es un número (ej. /api/clientes/abc) lanza
// IllegalArgumentException, que termina en 400.
    private Long leerId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo(); // la parte de la URL después de /api/clientes
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }
        try {
            return Long.valueOf(pathInfo.substring(1)); // quita la "/" inicial
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El id debe ser un número: " + pathInfo.substring(1));
        }
    }

    private boolean esJson(HttpServletRequest request) {
        String contentType = request.getContentType();
        return contentType != null && contentType.startsWith("application/json");
    }

    // Traduce cada tipo de error a su código HTTP (un solo lugar para todos los métodos).
    private void responderError(HttpServletResponse response, RuntimeException e)
            throws IOException {
        if (e instanceof ClienteNoEncontradoException) {
            escribirError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage()); // 404
        } else if (e instanceof ClienteDuplicadoException) {
            escribirError(response, HttpServletResponse.SC_CONFLICT, e.getMessage()); // 409
        } else if (e instanceof JacksonException) {
            escribirError(response, HttpServletResponse.SC_BAD_REQUEST, // 400
                    "El JSON enviado no es válido");
        } else if (e instanceof IllegalArgumentException) {
            escribirError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage()); // 400
        } else {
// Error inesperado (por ejemplo, la base de datos está apagada): se registra
// en la consola y al cliente solo se le dice lo mínimo.
            log("Error inesperado", e);
            escribirError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, // 500
                    "Error interno del servidor");
        }
    }

    private void escribirJson(HttpServletResponse response, int codigo, Object contenido)
            throws IOException {
        response.setStatus(codigo); // código de estado
        response.setContentType("application/json"); // cabecera Content-Type
        response.setCharacterEncoding("UTF-8"); // tildes y ñ
        jsonMapper.writeValue(response.getOutputStream(), contenido); // objeto -> JSON
    }

    private void escribirError(HttpServletResponse response, int codigo, String mensaje)
            throws IOException {
        escribirJson(response, codigo, Map.of("mensaje", mensaje));
    }
}