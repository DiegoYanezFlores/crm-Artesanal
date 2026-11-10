package edu.uio.crmartesanal.config;
import edu.uio.crmartesanal.cliente.controller.ClienteServlet;
import edu.uio.crmartesanal.cliente.service.ClienteService;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;
/**
 * Registra el Servlet en el servidor web (Tomcat) que trae Spring Boot.
 * Es el equivalente moderno de declarar el Servlet en web.xml.
 */
@Configuration
public class ClienteServletConfig {
    @Bean
    public ServletRegistrationBean<ClienteServlet> clienteServlet(
            ClienteService clienteService,
            JsonMapper jsonMapper) {
        ClienteServlet servlet = new ClienteServlet(clienteService, jsonMapper);
        ServletRegistrationBean<ClienteServlet> registro =
                new ServletRegistrationBean<>(servlet, "/api/clientes");
        registro.setName("clienteServlet");
        return registro;
    }
}
