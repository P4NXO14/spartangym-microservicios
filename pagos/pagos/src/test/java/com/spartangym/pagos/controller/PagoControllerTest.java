package com.spartangym.pagos.controller;

import com.spartangym.pagos.dto.PagoPlanDTO;
import com.spartangym.pagos.dto.PagoProductoDTO;
import com.spartangym.pagos.model.EstadoPago;
import com.spartangym.pagos.model.Pago;
import com.spartangym.pagos.model.TipoPago;
import com.spartangym.pagos.service.PagoService;

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

@WebMvcTest(PagoController.class) // Levanta solamente el Controller.
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Sirve para simular peticiones HTTP

    @MockitoBean // Crea un servicio falso. No se conecta a BD. No ejecuta logica real.
    private PagoService service;

    @Test
    void listarPagos() throws Exception {

        List<Pago> pagos = List.of(
                new Pago(1, 10, TipoPago.PLAN, 1, 29990.0, LocalDateTime.now(), EstadoPago.PAGADO)
        );

        when(service.listarTodos()).thenReturn(pagos);

        mockMvc.perform(get("/api/pagos"))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void buscarPagoPorId() throws Exception {

        Pago pago = new Pago(1, 10, TipoPago.PLAN, 1, 29990.0, LocalDateTime.now(), EstadoPago.PAGADO);

        when(service.buscarPorId(1)).thenReturn(pago);

        mockMvc.perform(get("/api/pagos/{id}", 1))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void listarPagosPorCliente() throws Exception {

        List<Pago> pagos = List.of(
                new Pago(1, 10, TipoPago.PLAN, 1, 29990.0, LocalDateTime.now(), EstadoPago.PAGADO)
        );

        when(service.listarPorCliente(10)).thenReturn(pagos);

        mockMvc.perform(get("/api/pagos/cliente/{idCliente}", 10))
                .andExpect(status().isOk());
    }

    @Test
    void listarPagosPorTipo() throws Exception {

        List<Pago> pagos = List.of(
                new Pago(1, 10, TipoPago.PLAN, 1, 29990.0, LocalDateTime.now(), EstadoPago.PAGADO)
        );

        when(service.listarPorTipo("PLAN")).thenReturn(pagos);

        mockMvc.perform(get("/api/pagos/tipo/{tipoPago}", "PLAN"))
                .andExpect(status().isOk());
    }

    // NUEVO TEST: REGISTRAR PAGO DE PLAN (POST)
    @Test
    void registrarPagoPlan() throws Exception {

        String pagoPlanJson = """
            {
                "idCliente": 10,
                "idPlan": 1
            }
            """;

        Pago pagoCreado = new Pago(2, 10, TipoPago.PLAN, 1, 29990.0, LocalDateTime.now(), EstadoPago.PAGADO);

        // Simulamos lo que haria el service
        when(service.registrarPagoPlan(any(PagoPlanDTO.class))).thenReturn(pagoCreado);

        mockMvc.perform(post("/api/pagos/plan")
                        .contentType(APPLICATION_JSON) // indica que enviamos JSON
                        .content(pagoPlanJson))         // cuerpo del request
                .andExpect(status().isCreated());       // validamos respuesta 201
    }

    // NUEVO TEST: REGISTRAR PAGO DE PRODUCTO (POST)
    @Test
    void registrarPagoProducto() throws Exception {

        String pagoProductoJson = """
            {
                "idCliente": 10,
                "referenciaId": 5,
                "montoCobrado": 15990.0
            }
            """;

        Pago pagoCreado = new Pago(3, 10, TipoPago.PRODUCTO, 5, 15990.0, LocalDateTime.now(), EstadoPago.PAGADO);

        when(service.registrarPagoProducto(any(PagoProductoDTO.class))).thenReturn(pagoCreado);

        mockMvc.perform(post("/api/pagos/producto")
                        .contentType(APPLICATION_JSON)
                        .content(pagoProductoJson))
                .andExpect(status().isCreated()); // validamos respuesta 201
    }

    // NUEVO TEST: CANCELAR PAGO (PUT)
    @Test
    void cancelarPago() throws Exception {

        Pago pagoCancelado = new Pago(1, 10, TipoPago.PLAN, 1, 29990.0, LocalDateTime.now(), EstadoPago.CANCELADO);

        when(service.cancelarPago(1)).thenReturn(pagoCancelado);

        mockMvc.perform(put("/api/pagos/cancelar/{id}", 1))
                .andExpect(status().isOk()); // validamos respuesta 200
    }

    // NUEVO TEST: ELIMINAR PAGO (DELETE)
    @Test
    void eliminarPago() throws Exception {

        mockMvc.perform(delete("/api/pagos/{id}", 1))
                .andExpect(status().isNoContent()); // validamos respuesta 204
    }
}
