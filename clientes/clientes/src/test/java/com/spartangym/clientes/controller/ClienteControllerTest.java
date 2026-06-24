package com.spartangym.clientes.controller;

import com.spartangym.clientes.model.Cliente;
import com.spartangym.clientes.model.Rol;
import com.spartangym.clientes.service.ClienteService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class) // Levanta solamente el Controller.
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de Spring Security para este test
public class ClienteControllerTest {
    
    @Autowired
    private MockMvc mockMvc; // Sirve para simular peticiones HTTP

   @MockBean // Crea un servicio falso. No se conecta a BD. No ejecuta logica real.
    private ClienteService service;

    @Test
    void listarClientes() throws Exception {

        List<Cliente> clientes = List.of(
                new Cliente(1, "12345678-9", "Juan Perez", "juan.perez@correo.com",
                        "claveSegura123", "+56912345678", "Activo", Rol.CLIENTE)
        );

        when(service.listarTodos()).thenReturn(clientes);

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void buscarClientePorId() throws Exception {

        Cliente cliente = new Cliente(1, "12345678-9", "Juan Perez", "juan.perez@correo.com",
                "claveSegura123", "+56912345678", "Activo", Rol.CLIENTE);

        when(service.buscarPorId(1)).thenReturn(cliente);

        mockMvc.perform(get("/api/clientes/{id}", 1))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    // NUEVO TEST: CREAR CLIENTE (POST)
    @Test
    void crearCliente() throws Exception {

        String clienteJson = """
            {
                "rut": "98765432-1",
                "nombreCompleto": "Maria Gonzalez",
                "email": "maria.gonzalez@correo.com",
                "password": "otraClaveSegura",
                "telefono": "+56987654321",
                "estado": "Activo",
                "rol": "CLIENTE"
            }
            """;

        Cliente clienteCreado = new Cliente(2, "98765432-1", "Maria Gonzalez", "maria.gonzalez@correo.com",
                "otraClaveSegura", "+56987654321", "Activo", Rol.CLIENTE);

        // Simulamos lo que haria el service
        when(service.guardarCliente(any(Cliente.class))).thenReturn(clienteCreado);

        mockMvc.perform(post("/api/clientes/crear")
                        .contentType(APPLICATION_JSON) // indica que enviamos JSON
                        .content(clienteJson))         // cuerpo del request
                .andExpect(status().isCreated());      // validamos respuesta 201
    }

    // NUEVO TEST: ACTUALIZAR CLIENTE (PUT)
    @Test
    void actualizarCliente() throws Exception {

        String clienteJson = """
            {
                "rut": "12345678-9",
                "nombreCompleto": "Juan Perez Actualizado",
                "email": "juan.perez@correo.com",
                "telefono": "+56912345678",
                "estado": "Activo",
                "rol": "CLIENTE"
            }
            """;

        Cliente clienteActualizado = new Cliente(1, "12345678-9", "Juan Perez Actualizado", "juan.perez@correo.com",
                "claveSegura123", "+56912345678", "Activo", Rol.CLIENTE);

        when(service.actualizar(eq(1), any(Cliente.class))).thenReturn(clienteActualizado);

        mockMvc.perform(put("/api/clientes/actualizar/{id}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(clienteJson))
                .andExpect(status().isOk()); // validamos respuesta 200
    }

    // NUEVO TEST: ELIMINAR CLIENTE (DELETE)
    @Test
    void eliminarCliente() throws Exception {

        mockMvc.perform(delete("/api/clientes/{id}", 1))
                .andExpect(status().isOk()); // validamos respuesta 200
    }
}