package com.spartangym.productos.controller;

import com.spartangym.productos.dto.VentaDTO;
import com.spartangym.productos.model.EstadoVenta;
import com.spartangym.productos.model.Producto;
import com.spartangym.productos.model.Venta;
import com.spartangym.productos.service.VentaService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VentaController.class) // Levanta solamente el Controller.
public class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc; // Sirve para simular peticiones HTTP

    @MockitoBean // Crea un servicio falso. No se conecta a BD. No ejecuta logica real.
    private VentaService service;

    @Test
    void listarVentas() throws Exception {

        Producto producto = new Producto(5, "Bebida isotonica", 2500.0, 50);
        List<Venta> ventas = List.of(
                new Venta(1, producto, 10, 2, 5000.0, LocalDateTime.now(), EstadoVenta.REALIZADA)
        );

        when(service.listarTodas()).thenReturn(ventas);

        mockMvc.perform(get("/api/ventas"))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void buscarVentaPorId() throws Exception {

        Producto producto = new Producto(5, "Bebida isotonica", 2500.0, 50);
        Venta venta = new Venta(1, producto, 10, 2, 5000.0, LocalDateTime.now(), EstadoVenta.REALIZADA);

        when(service.buscarPorId(1)).thenReturn(venta);

        mockMvc.perform(get("/api/ventas/{id}", 1))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void listarVentasPorProducto() throws Exception {

        Producto producto = new Producto(5, "Bebida isotonica", 2500.0, 50);
        List<Venta> ventas = List.of(
                new Venta(1, producto, 10, 2, 5000.0, LocalDateTime.now(), EstadoVenta.REALIZADA)
        );

        when(service.listarPorProducto(5)).thenReturn(ventas);

        mockMvc.perform(get("/api/ventas/producto/{idProducto}", 5))
                .andExpect(status().isOk());
    }

    // NUEVO TEST: REGISTRAR VENTA (POST)
    @Test
    void registrarVenta() throws Exception {

        String ventaJson = """
            {
                "idCliente": 10,
                "cantidad": 2
            }
            """;

        Producto producto = new Producto(5, "Bebida isotonica", 2500.0, 48);
        Venta ventaCreada = new Venta(2, producto, 10, 2, 5000.0, LocalDateTime.now(), EstadoVenta.REALIZADA);

        // Simulamos lo que haria el service
        when(service.vender(eq(5), any(VentaDTO.class))).thenReturn(ventaCreada);

        mockMvc.perform(post("/api/ventas/producto/{idProducto}", 5)
                        .contentType(APPLICATION_JSON) // indica que enviamos JSON
                        .content(ventaJson))            // cuerpo del request
                .andExpect(status().isCreated());       // validamos respuesta 201
    }

    // NUEVO TEST: CANCELAR VENTA (PUT)
    @Test
    void cancelarVenta() throws Exception {

        Producto producto = new Producto(5, "Bebida isotonica", 2500.0, 50);
        Venta ventaCancelada = new Venta(1, producto, 10, 2, 5000.0, LocalDateTime.now(), EstadoVenta.CANCELADA);

        when(service.cancelarVenta(1)).thenReturn(ventaCancelada);

        mockMvc.perform(put("/api/ventas/cancelar/{id}", 1))
                .andExpect(status().isOk()); // validamos respuesta 200
    }

    // NUEVO TEST: ELIMINAR VENTA (DELETE)
    @Test
    void eliminarVenta() throws Exception {

        mockMvc.perform(delete("/api/ventas/{id}", 1))
                .andExpect(status().isNoContent()); // validamos respuesta 204
    }
}
