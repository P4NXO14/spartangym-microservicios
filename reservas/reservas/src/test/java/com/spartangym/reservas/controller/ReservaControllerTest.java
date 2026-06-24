package com.spartangym.reservas.controller;

import com.spartangym.reservas.model.Clase;
import com.spartangym.reservas.model.Reserva;
import com.spartangym.reservas.service.ReservaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservaController.class) // Levanta solamente el Controller.
public class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Sirve para simular peticiones HTTP

    @MockitoBean // Crea un servicio falso. No se conecta a BD. No ejecuta logica real.
    private ReservaService service;

    @Test
    void listarReservas() throws Exception {

        Clase clase = new Clase(1, "Spinning", 20, 15, LocalDate.now().plusDays(1), LocalTime.of(9, 0));
        List<Reserva> reservas = List.of(
                new Reserva(1, clase, 10, LocalDateTime.now(), "CONFIRMADA")
        );

        when(service.listaReservas()).thenReturn(reservas);

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    // NUEVO TEST: GENERAR RESERVA (POST con RequestParam, no body)
    @Test
    void generarReserva() throws Exception {

        Clase clase = new Clase(1, "Spinning", 20, 14, LocalDate.now().plusDays(1), LocalTime.of(9, 0));
        Reserva reservaCreada = new Reserva(2, clase, 10, LocalDateTime.now(), "CONFIRMADA");

        // Simulamos lo que haria el service
        when(service.generarReserva(10, 1)).thenReturn(reservaCreada);

        mockMvc.perform(post("/api/reservas")
                        .param("clienteId", "10") // los parametros van en la query, no en el body
                        .param("claseId", "1"))
                .andExpect(status().isCreated()); // validamos respuesta 201
    }

    // NUEVO TEST: CANCELAR RESERVA (PUT)
    @Test
    void cancelarReserva() throws Exception {

        Clase clase = new Clase(1, "Spinning", 20, 15, LocalDate.now().plusDays(1), LocalTime.of(9, 0));
        Reserva reservaCancelada = new Reserva(1, clase, 10, LocalDateTime.now(), "CANCELADA");

        when(service.cancelarReserva(1)).thenReturn(reservaCancelada);

        mockMvc.perform(put("/api/reservas/cancelar/{id}", 1))
                .andExpect(status().isOk()); // validamos respuesta 200
    }

    // NUEVO TEST: ELIMINAR RESERVA (DELETE)
    @Test
    void eliminarReserva() throws Exception {

        mockMvc.perform(delete("/api/reservas/{id}", 1))
                .andExpect(status().isNoContent()); // validamos respuesta 204
    }
}
