package com.spartangym.notificaciones.controller;

import com.spartangym.notificaciones.model.Notificacion;
import com.spartangym.notificaciones.service.NotificacionService;
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

@WebMvcTest(NotificacionController.class) // Levanta solamente el Controller.
public class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc; // Sirve para simular peticiones HTTP

    @MockitoBean // Crea un servicio falso. No se conecta a BD. No ejecuta logica real.
    private NotificacionService service;

    @Test
    void listarNotificaciones() throws Exception {

        List<Notificacion> notificaciones = List.of(
                new Notificacion(1, 10, "Recordatorio de pago", "Tu plan vence pronto", LocalDateTime.now())
        );

        when(service.listarTodas()).thenReturn(notificaciones);

        mockMvc.perform(get("/api/notificaciones"))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void buscarNotificacionPorId() throws Exception {

        Notificacion notificacion = new Notificacion(1, 10, "Recordatorio de pago", "Tu plan vence pronto", LocalDateTime.now());

        when(service.buscarPorId(1)).thenReturn(notificacion);

        mockMvc.perform(get("/api/notificaciones/{id}", 1))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void listarNotificacionesPorCliente() throws Exception {

        List<Notificacion> notificaciones = List.of(
                new Notificacion(1, 10, "Recordatorio de pago", "Tu plan vence pronto", LocalDateTime.now())
        );

        when(service.listarPorCliente(10)).thenReturn(notificaciones);

        mockMvc.perform(get("/api/notificaciones/cliente/{idCliente}", 10))
                .andExpect(status().isOk());
    }

    // NUEVO TEST: REGISTRAR NOTIFICACION (POST)
    @Test
    void registrarNotificacion() throws Exception {

        String notificacionJson = """
            {
                "idCliente": 10,
                "titulo": "Nueva clase disponible",
                "mensaje": "Se agrego una nueva clase de spinning"
            }
            """;

        Notificacion notificacionCreada = new Notificacion(
                2, 10, "Nueva clase disponible", "Se agrego una nueva clase de spinning", LocalDateTime.now()
        );

        // Simulamos lo que haria el service
        when(service.guardar(any(Notificacion.class))).thenReturn(notificacionCreada);

        mockMvc.perform(post("/api/notificaciones")
                        .contentType(APPLICATION_JSON) // indica que enviamos JSON
                        .content(notificacionJson))     // cuerpo del request
                .andExpect(status().isCreated());       // validamos respuesta 201
    }

    // NUEVO TEST: ELIMINAR NOTIFICACION (DELETE)
    @Test
    void eliminarNotificacion() throws Exception {

        mockMvc.perform(delete("/api/notificaciones/{id}", 1))
                .andExpect(status().isNoContent()); // validamos respuesta 204
    }
}