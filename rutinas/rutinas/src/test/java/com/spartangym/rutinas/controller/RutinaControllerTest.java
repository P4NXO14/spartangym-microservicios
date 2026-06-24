package com.spartangym.rutinas.controller;

import com.spartangym.rutinas.model.Rutina;
import com.spartangym.rutinas.service.RutinaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RutinaController.class) // Levanta solamente el Controller.
public class RutinaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Sirve para simular peticiones HTTP

    @MockitoBean // Crea un servicio falso. No se conecta a BD. No ejecuta logica real.
    private RutinaService service;

    @Test
    void listarRutinas() throws Exception {

        List<Rutina> rutinas = List.of(
                new Rutina(1, 10, "Rutina de fuerza", "Entrenamiento de tren superior", "Ganar masa muscular", "Intermedio", LocalDate.now())
        );

        when(service.listarTodas()).thenReturn(rutinas);

        mockMvc.perform(get("/api/rutinas"))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void buscarRutinaPorId() throws Exception {

        Rutina rutina = new Rutina(1, 10, "Rutina de fuerza", "Entrenamiento de tren superior", "Ganar masa muscular", "Intermedio", LocalDate.now());

        when(service.buscarPorId(1)).thenReturn(rutina);

        mockMvc.perform(get("/api/rutinas/{id}", 1))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void listarRutinasPorCliente() throws Exception {

        List<Rutina> rutinas = List.of(
                new Rutina(1, 10, "Rutina de fuerza", "Entrenamiento de tren superior", "Ganar masa muscular", "Intermedio", LocalDate.now())
        );

        when(service.listarPorCliente(10)).thenReturn(rutinas);

        mockMvc.perform(get("/api/rutinas/cliente/{idCliente}", 10))
                .andExpect(status().isOk());
    }

    // NUEVO TEST: REGISTRAR RUTINA (POST)
    @Test
    void registrarRutina() throws Exception {

        String rutinaJson = """
            {
                "idCliente": 10,
                "nombreRutina": "Rutina de resistencia",
                "descripcion": "Entrenamiento cardiovascular",
                "objetivo": "Mejorar resistencia",
                "dificultad": "Avanzado"
            }
            """;

        Rutina rutinaCreada = new Rutina(2, 10, "Rutina de resistencia", "Entrenamiento cardiovascular", "Mejorar resistencia", "Avanzado", LocalDate.now());

        // Simulamos lo que haria el service
        when(service.guardar(any(Rutina.class))).thenReturn(rutinaCreada);

        mockMvc.perform(post("/api/rutinas")
                        .contentType(APPLICATION_JSON) // indica que enviamos JSON
                        .content(rutinaJson))           // cuerpo del request
                .andExpect(status().isCreated());       // validamos respuesta 201
    }

    // NUEVO TEST: ACTUALIZAR RUTINA (PUT)
    @Test
    void actualizarRutina() throws Exception {

        String rutinaJson = """
            {
                "idCliente": 10,
                "nombreRutina": "Rutina de fuerza avanzada",
                "descripcion": "Entrenamiento de tren superior e inferior",
                "objetivo": "Ganar masa muscular",
                "dificultad": "Avanzado"
            }
            """;

        Rutina rutinaActualizada = new Rutina(1, 10, "Rutina de fuerza avanzada", "Entrenamiento de tren superior e inferior", "Ganar masa muscular", "Avanzado", LocalDate.now());

        when(service.actualizar(eq(1), any(Rutina.class))).thenReturn(rutinaActualizada);

        mockMvc.perform(put("/api/rutinas/{id}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(rutinaJson))
                .andExpect(status().isOk()); // validamos respuesta 200
    }

    // NUEVO TEST: ELIMINAR RUTINA (DELETE)
    @Test
    void eliminarRutina() throws Exception {

        mockMvc.perform(delete("/api/rutinas/{id}", 1))
                .andExpect(status().isNoContent()); // validamos respuesta 204
    }
}
