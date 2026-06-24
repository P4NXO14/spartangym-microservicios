package com.spartangym.planes.controller;

import com.spartangym.planes.model.Plan;
import com.spartangym.planes.service.PlanService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlanController.class) // Levanta solamente el Controller.
public class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc; // Sirve para simular peticiones HTTP

    @MockitoBean // Crea un servicio falso. No se conecta a BD. No ejecuta logica real.
    private PlanService service;

    @Test
    void listarPlanes() throws Exception {

        List<Plan> planes = List.of(
                new Plan(1, "Plan Mensual", "Acceso ilimitado por 30 dias", 29990.0, 30)
        );

        when(service.listarTodos()).thenReturn(planes);

        mockMvc.perform(get("/api/planes"))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void buscarPlanPorId() throws Exception {

        Plan plan = new Plan(1, "Plan Mensual", "Acceso ilimitado por 30 dias", 29990.0, 30);

        when(service.buscarPorId(1)).thenReturn(plan);

        mockMvc.perform(get("/api/planes/{id}", 1))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    // NUEVO TEST: REGISTRAR PLAN (POST)
    @Test
    void registrarPlan() throws Exception {

        String planJson = """
            {
                "nombrePlan": "Plan Anual",
                "descripcion": "Acceso ilimitado por 365 dias",
                "precio": 299990.0,
                "duracionDias": 365
            }
            """;

        Plan planCreado = new Plan(2, "Plan Anual", "Acceso ilimitado por 365 dias", 299990.0, 365);

        // Simulamos lo que haria el service
        when(service.guardar(any(Plan.class))).thenReturn(planCreado);

        mockMvc.perform(post("/api/planes")
                        .contentType(APPLICATION_JSON) // indica que enviamos JSON
                        .content(planJson))             // cuerpo del request
                .andExpect(status().isCreated());       // validamos respuesta 201
    }

    // NUEVO TEST: ACTUALIZAR PLAN (PUT)
    @Test
    void actualizarPlan() throws Exception {

        String planJson = """
            {
                "nombrePlan": "Plan Mensual Promo",
                "descripcion": "Acceso ilimitado por 30 dias con descuento",
                "precio": 24990.0,
                "duracionDias": 30
            }
            """;

        Plan planActualizado = new Plan(1, "Plan Mensual Promo", "Acceso ilimitado por 30 dias con descuento", 24990.0, 30);

        when(service.actualizar(eq(1), any(Plan.class))).thenReturn(planActualizado);

        mockMvc.perform(put("/api/planes/{id}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(planJson))
                .andExpect(status().isOk()); // validamos respuesta 200
    }

    // NUEVO TEST: ELIMINAR PLAN (DELETE)
    @Test
    void eliminarPlan() throws Exception {

        mockMvc.perform(delete("/api/planes/{id}", 1))
                .andExpect(status().isNoContent()); // validamos respuesta 204
    }
}
