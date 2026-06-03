package com.spartangym.pagos.controller;

import com.spartangym.pagos.dto.PagoPlanDTO;
import com.spartangym.pagos.dto.PagoProductoDTO;
import com.spartangym.pagos.model.Pago;
import com.spartangym.pagos.service.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> listar() {
        return ResponseEntity.ok(pagoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Pago>> listarPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(pagoService.listarPorCliente(idCliente));
    }

    @GetMapping("/tipo/{tipoPago}")
    public ResponseEntity<List<Pago>> listarPorTipo(@PathVariable String tipoPago) {
        return ResponseEntity.ok(pagoService.listarPorTipo(tipoPago));
    }

    @PostMapping("/plan")
    public ResponseEntity<Pago> registrarPagoPlan(@Valid @RequestBody PagoPlanDTO pagoPlanDTO) {
        Pago pago = pagoService.registrarPagoPlan(pagoPlanDTO);
        return ResponseEntity.status(201).body(pago);
    }

    @PostMapping("/producto")
    public ResponseEntity<Pago> registrarPagoProducto(@Valid @RequestBody PagoProductoDTO pagoProductoDTO) {
        Pago pago = pagoService.registrarPagoProducto(pagoProductoDTO);
        return ResponseEntity.status(201).body(pago);
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Pago> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(pagoService.cancelarPago(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}