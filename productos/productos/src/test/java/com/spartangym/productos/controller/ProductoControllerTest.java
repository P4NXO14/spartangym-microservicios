package com.spartangym.productos.controller;

import com.spartangym.productos.model.Producto;
import com.spartangym.productos.service.ProductoService;

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

@WebMvcTest(ProductoController.class) // Levanta solamente el Controller.
public class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc; // Sirve para simular peticiones HTTP

    @MockitoBean // Crea un servicio falso. No se conecta a BD. No ejecuta logica real.
    private ProductoService service;

    @Test
    void listarProductos() throws Exception {

        List<Producto> productos = List.of(
                new Producto(1, "Bebida isotonica", 2500.0, 50)
        );

        when(service.listarTodos()).thenReturn(productos);

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    @Test
    void buscarProductoPorId() throws Exception {

        Producto producto = new Producto(1, "Bebida isotonica", 2500.0, 50);

        when(service.buscarPorId(1)).thenReturn(producto);

        mockMvc.perform(get("/api/productos/{id}", 1))
                .andExpect(status().isOk()); // verifica que el endpoint respondio 200
    }

    // NUEVO TEST: REGISTRAR PRODUCTO (POST)
    @Test
    void registrarProducto() throws Exception {

        String productoJson = """
            {
                "nombre": "Proteina en polvo",
                "precio": 25990.0,
                "stock": 20
            }
            """;

        Producto productoCreado = new Producto(2, "Proteina en polvo", 25990.0, 20);

        // Simulamos lo que haria el service
        when(service.guardar(any(Producto.class))).thenReturn(productoCreado);

        mockMvc.perform(post("/api/productos")
                        .contentType(APPLICATION_JSON) // indica que enviamos JSON
                        .content(productoJson))         // cuerpo del request
                .andExpect(status().isCreated());       // validamos respuesta 201
    }

    // NUEVO TEST: ACTUALIZAR PRODUCTO (PUT)
    @Test
    void actualizarProducto() throws Exception {

        String productoJson = """
            {
                "nombre": "Bebida isotonica 600ml",
                "precio": 2800.0,
                "stock": 45
            }
            """;

        Producto productoActualizado = new Producto(1, "Bebida isotonica 600ml", 2800.0, 45);

        when(service.actualizar(eq(1), any(Producto.class))).thenReturn(productoActualizado);

        mockMvc.perform(put("/api/productos/{id}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(productoJson))
                .andExpect(status().isOk()); // validamos respuesta 200
    }

    // NUEVO TEST: ELIMINAR PRODUCTO (DELETE)
    @Test
    void eliminarProducto() throws Exception {

        mockMvc.perform(delete("/api/productos/{id}", 1))
                .andExpect(status().isNoContent()); // validamos respuesta 204
    }
}
