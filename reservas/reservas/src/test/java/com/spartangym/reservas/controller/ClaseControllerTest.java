package com.spartangym.reservas.controller;

import com.spartangym.reservas.model.Clase;
import com.spartangym.reservas.service.ClaseService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClaseController.class) // Levanta solamente el Controller.
public class ClaseControllerTest {

    @Autowired
    private MockMvc mockMvc; // Sirve para simular peticiones HTTP

    @MockitoBean // Crea un servicio falso. No se conecta a BD. No ejecuta logica real.
    private ClaseService service;

    @Test
    void listarClases() throws Exception {

        List<Clase> clases = List.of(
                new Clase(1, "Spinning", 20, 15, LocalDate.now().plusDays(1), LocalTime.of(9, 0))
        );

        when(service.listarTodas()).thenReturn(clases);

        mockMvc.perform(get("/api/clases"))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void buscarClasePorId() throws Exception {

        Clase clase = new Clase(1, "Spinning", 20, 15, LocalDate.now().plusDays(1), LocalTime.of(9, 0));

        when(service.buscarPorId(1)).thenReturn(clase);

        mockMvc.perform(get("/api/clases/{id}", 1))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    // NUEVO TEST: REGISTRAR CLASE (POST)
    @Test
    void registrarClase() throws Exception {

        String claseJson = """
            {
                "nombreClase": "Yoga",
                "cuposTotales": 15,
                "cuposDisponibles": 15,
                "fecha": "2026-07-01",
                "hora": "18:00:00"
            }
            """;

        Clase claseCreada = new Clase(2, "Yoga", 15, 15, LocalDate.of(2026, 7, 1), LocalTime.of(18, 0));

        // Simulamos lo que haria el service
        when(service.guardar(any(Clase.class))).thenReturn(claseCreada);

        mockMvc.perform(post("/api/clases")
                        .contentType(APPLICATION_JSON) // indica que enviamos JSON
                        .content(claseJson))            // cuerpo del request
                .andExpect(status().isCreated());       // validamos respuesta 201
    }

    // NUEVO TEST: ACTUALIZAR CLASE (PUT)
    @Test
    void actualizarClase() throws Exception {

        String claseJson = """
            {
                "nombreClase": "Spinning Avanzado",
                "cuposTotales": 20,
                "cuposDisponibles": 18,
                "fecha": "2026-07-02",
                "hora": "09:00:00"
            }
            """;

        Clase claseActualizada = new Clase(1, "Spinning Avanzado", 20, 18, LocalDate.of(2026, 7, 2), LocalTime.of(9, 0));

        when(service.actualizar(eq(1), any(Clase.class))).thenReturn(claseActualizada);

        mockMvc.perform(put("/api/clases/{id}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(claseJson))
                .andExpect(status().isOk()); // validamos respuesta 200
    }

    // NUEVO TEST: ELIMINAR CLASE (DELETE)
    @Test
    void eliminarClase() throws Exception {

        mockMvc.perform(delete("/api/clases/eliminar/{id}", 1))
                .andExpect(status().isNoContent()); // validamos respuesta 204
    }
}
