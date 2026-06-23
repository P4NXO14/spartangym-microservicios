package com.spartangym.productos.controller;

import com.spartangym.productos.dto.VentaDTO;
import com.spartangym.productos.model.Venta;
import com.spartangym.productos.service.VentaService;
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
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Tag(name = "Ventas", description = "Registro y control de ventas de productos del gimnasio")
public class VentaController {

    private final VentaService ventaService;

    @Operation(
            summary = "Listar ventas",
            description = "Obtiene la lista con todas las ventas registradas en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Venta>> listar() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    @Operation(
            summary = "Buscar venta por ID",
            description = "Obtiene los datos de una venta especifica a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.buscarPorId(id));
    }

    @Operation(
            summary = "Registrar venta",
            description = "Permite registrar la venta de un producto a un cliente, descontando el stock disponible"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venta registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la venta invalidos o stock insuficiente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/producto/{idProducto}")
    public ResponseEntity<Venta> vender(
            @PathVariable Integer idProducto,
            @Valid @RequestBody VentaDTO ventaDTO) {

        Venta venta = ventaService.vender(idProducto, ventaDTO);
        return ResponseEntity.status(201).body(venta);
    }

    @Operation(
            summary = "Listar ventas por producto",
            description = "Obtiene todas las ventas asociadas a un producto especifico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<Venta>> listarPorProducto(@PathVariable Integer idProducto) {
        return ResponseEntity.ok(ventaService.listarPorProducto(idProducto));
    }

    @Operation(
            summary = "Cancelar venta",
            description = "Permite cancelar una venta previamente registrada y reponer el stock"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venta cancelada correctamente"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Venta> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.cancelarVenta(id));
    }

    @Operation(
            summary = "Eliminar venta",
            description = "Elimina un registro de venta a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venta eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}