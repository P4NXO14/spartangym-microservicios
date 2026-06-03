package com.spartangym.productos.controller;

import com.spartangym.productos.dto.VentaDTO;
import com.spartangym.productos.model.Venta;
import com.spartangym.productos.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @GetMapping
    public ResponseEntity<List<Venta>> listar() {
        return ResponseEntity.ok(ventaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.buscarPorId(id));
    }

    @PostMapping("/producto/{idProducto}")
    public ResponseEntity<Venta> vender(
            @PathVariable Integer idProducto,
            @Valid @RequestBody VentaDTO ventaDTO) {

        Venta venta = ventaService.vender(idProducto, ventaDTO);
        return ResponseEntity.status(201).body(venta);
    }

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<Venta>> listarPorProducto(@PathVariable Integer idProducto) {
        return ResponseEntity.ok(ventaService.listarPorProducto(idProducto));
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Venta> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(ventaService.cancelarVenta(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ventaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}