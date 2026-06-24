package com.spartangym.logros.controller;

import com.spartangym.logros.model.Logro;
import com.spartangym.logros.service.LogroService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LogroController.class) // Levanta solamente el Controller.
public class LogroControllerTest {

    @Autowired
    private MockMvc mockMvc; // Sirve para simular peticiones HTTP

    @MockitoBean // Crea un servicio falso. No se conecta a BD. No ejecuta logica real.
    private LogroService service;

    @Test
    void listarLogros() throws Exception {

        List<Logro> logros = List.of(
                new Logro(1, 10, "Primer mes completo", "Asistio todo un mes seguido", LocalDate.now())
        );

        when(service.listarTodos()).thenReturn(logros);

        mockMvc.perform(get("/api/logros"))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void buscarLogroPorId() throws Exception {

        Logro logro = new Logro(1, 10, "Primer mes completo", "Asistio todo un mes seguido", LocalDate.now());

        when(service.buscarPorId(1)).thenReturn(logro);

        mockMvc.perform(get("/api/logros/{id}", 1))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void listarLogrosPorCliente() throws Exception {

        List<Logro> logros = List.of(
                new Logro(1, 10, "Primer mes completo", "Asistio todo un mes seguido", LocalDate.now())
        );

        when(service.listarPorCliente(10)).thenReturn(logros);

        mockMvc.perform(get("/api/logros/cliente/{idCliente}", 10))
                .andExpect(status().isOk());
    }

    // NUEVO TEST: REGISTRAR LOGRO (POST)
    @Test
    void registrarLogro() throws Exception {

        String logroJson = """
            {
                "idCliente": 10,
                "nombreLogro": "10 kilos perdidos",
                "descripcion": "El cliente logro bajar 10 kilos"
            }
            """;

        Logro logroCreado = new Logro(2, 10, "10 kilos perdidos", "El cliente logro bajar 10 kilos", LocalDate.now());

        // Simulamos lo que haria el service
        when(service.guardar(any(Logro.class))).thenReturn(logroCreado);

        mockMvc.perform(post("/api/logros")
                        .contentType(APPLICATION_JSON) // indica que enviamos JSON
                        .content(logroJson))           // cuerpo del request
                .andExpect(status().isCreated());      // validamos respuesta 201
    }

    // NUEVO TEST: ELIMINAR LOGRO (DELETE)
    @Test
    void eliminarLogro() throws Exception {

        mockMvc.perform(delete("/api/logros/{id}", 1))
                .andExpect(status().isNoContent()); // validamos respuesta 204
    }
}
