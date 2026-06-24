package com.spartangym.asistencia.controller;

import com.spartangym.asistencia.model.Asistencia;
import com.spartangym.asistencia.model.EstadoAsistencia;
import com.spartangym.asistencia.service.AsistenciaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AsistenciaController.class) // Levanta solamente el Controller.
public class AsistenciaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Sirve para simular peticiones HTTP

    @MockitoBean // Crea un servicio falso. No se conecta a BD. No ejecuta logica real.
    private AsistenciaService service;

    @Test
    void listarAsistencias() throws Exception {

        List<Asistencia> asistencias = List.of(
                new Asistencia(1, 10, LocalDateTime.now(), null, EstadoAsistencia.REGISTRADA)
        );

        when(service.listarTodas()).thenReturn(asistencias);

        mockMvc.perform(get("/api/asistencia"))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void buscarAsistenciaPorId() throws Exception {

        Asistencia asistencia = new Asistencia(1, 10, LocalDateTime.now(), null, EstadoAsistencia.REGISTRADA);

        when(service.buscarPorId(1)).thenReturn(asistencia);

        mockMvc.perform(get("/api/asistencia/{id}", 1))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void listarAsistenciasPorCliente() throws Exception {

        List<Asistencia> asistencias = List.of(
                new Asistencia(1, 10, LocalDateTime.now(), null, EstadoAsistencia.REGISTRADA)
        );

        when(service.listarPorCliente(10)).thenReturn(asistencias);

        mockMvc.perform(get("/api/asistencia/cliente/{idCliente}", 10))
                .andExpect(status().isOk());
    }

    @Test
    void listarAsistenciasPorEstado() throws Exception {

        List<Asistencia> asistencias = List.of(
                new Asistencia(1, 10, LocalDateTime.now(), null, EstadoAsistencia.REGISTRADA)
        );

        when(service.listarPorEstado("REGISTRADA")).thenReturn(asistencias);

        mockMvc.perform(get("/api/asistencia/estado/{estado}", "REGISTRADA"))
                .andExpect(status().isOk());
    }

    // NUEVO TEST: REGISTRAR INGRESO (POST)
    @Test
    void registrarIngreso() throws Exception {

        String asistenciaJson = """
            {
                "idCliente": 10
            }
            """;

        Asistencia asistenciaCreada = new Asistencia(2, 10, LocalDateTime.now(), null, EstadoAsistencia.REGISTRADA);

        // Simulamos lo que haria el service
        when(service.registrarIngreso(any(Asistencia.class))).thenReturn(asistenciaCreada);

        mockMvc.perform(post("/api/asistencia/crear")
                        .contentType(APPLICATION_JSON) // indica que enviamos JSON
                        .content(asistenciaJson))       // cuerpo del request
                .andExpect(status().isCreated());       // validamos respuesta 201
    }

    // NUEVO TEST: REGISTRAR SALIDA (PUT)
    @Test
    void registrarSalida() throws Exception {

        Asistencia asistenciaConSalida = new Asistencia(
                1, 10, LocalDateTime.now().minusHours(1), LocalDateTime.now(), EstadoAsistencia.COMPLETADA
        );

        when(service.registrarSalida(1)).thenReturn(asistenciaConSalida);

        mockMvc.perform(put("/api/asistencia/salida/{id}", 1))
                .andExpect(status().isOk()); // validamos respuesta 200
    }

    // NUEVO TEST: CANCELAR ASISTENCIA (PUT)
    @Test
    void cancelarAsistencia() throws Exception {

        Asistencia asistenciaCancelada = new Asistencia(
                1, 10, LocalDateTime.now().minusHours(1), LocalDateTime.now(), EstadoAsistencia.CANCELADA
        );

        when(service.cancelarAsistencia(1)).thenReturn(asistenciaCancelada);

        mockMvc.perform(put("/api/asistencia/cancelar/{id}", 1))
                .andExpect(status().isOk()); // validamos respuesta 200
    }

    // NUEVO TEST: ELIMINAR ASISTENCIA (DELETE)
    @Test
    void eliminarAsistencia() throws Exception {

        mockMvc.perform(delete("/api/asistencia/eliminar/{id}", 1))
                .andExpect(status().isNoContent()); // validamos respuesta 204
    }
}