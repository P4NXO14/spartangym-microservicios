package com.spartangym.reservas.controller;

import com.spartangym.reservas.model.Reserva;
import com.spartangym.reservas.service.ReservaService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        return ResponseEntity.ok(reservaService.listaReservas());
    }

    @GetMapping("/{idReserva}")
    public ResponseEntity<Reserva> buscarPorId(@PathVariable Integer idReserva) {
        return ResponseEntity.ok(reservaService.buscarPorId(idReserva));
    }

    @PostMapping("/crear")
    public ResponseEntity<Reserva> crearReserva(
            @RequestParam Integer clienteId,
            @RequestParam Integer claseId) {

        Reserva reserva = reservaService.generarReserva(clienteId, claseId);
        return ResponseEntity.status(201).body(reserva);
    }

    @PutMapping("/cancelar/{idReserva}")
    public ResponseEntity<Reserva> cancelarReserva(@PathVariable Integer idReserva) {
        Reserva reservaCancelada = reservaService.cancelarReserva(idReserva);
        return ResponseEntity.ok(reservaCancelada);
    }

    @DeleteMapping("/eliminar/{idReserva}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer idReserva) {
        reservaService.eliminarReserva(idReserva);
        return ResponseEntity.noContent().build();
    }
}