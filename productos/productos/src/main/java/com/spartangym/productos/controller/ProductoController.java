package com.spartangym.productos.controller;

import com.spartangym.productos.model.Producto;
import com.spartangym.productos.service.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "Gestion del inventario de productos del gimnasio")
public class ProductoController {

    private final ProductoService productoService;

    @Operation(
            summary = "Listar productos",
            description = "Obtiene la lista con todos los productos disponibles en el inventario"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @Operation(
            summary = "Buscar producto por ID",
            description = "Obtiene los datos de un producto especifico a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @Operation(
            summary = "Registrar producto",
            description = "Permite registrar un nuevo producto en el inventario"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del producto invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Producto> guardar(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(201).body(nuevoProducto);
    }

    @Operation(
            summary = "Actualizar producto",
            description = "Permite actualizar los datos de un producto existente a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del producto invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Producto producto) {

        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto del inventario a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}