package edu.uio.crmartesanal.cliente.controller;

import edu.uio.crmartesanal.cliente.dto.ClienteRequest;
import edu.uio.crmartesanal.cliente.model.Cliente;
import edu.uio.crmartesanal.cliente.service.ClienteDuplicadoException;
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
 * Capa de presentación con un Servlet "clásico".
 * Atiende la dirección /api/clientes (se registra en ClienteServletConfig).
 * Aquí TODO el trabajo HTTP se hace a mano: leer el cuerpo, convertir JSON,
 * fijar cabeceras y códigos de estado.
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
// Métodos de apoyo para construir la respuesta
// ==========================================================
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

