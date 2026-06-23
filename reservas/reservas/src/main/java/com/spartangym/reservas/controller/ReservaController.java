package com.spartangym.reservas.controller;

import com.spartangym.reservas.model.Reserva;
import com.spartangym.reservas.service.ReservaService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Gestion de reservas de clases por parte de los clientes del gimnasio")
public class ReservaController {

    private final ReservaService reservaService;

    @Operation(
            summary = "Listar reservas",
            description = "Obtiene la lista con todas las reservas registradas en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Reserva>> listarReservas() {
        return ResponseEntity.ok(reservaService.listaReservas());
    }

    @Operation(
            summary = "Buscar reserva por ID",
            description = "Obtiene los datos de una reserva especifica a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{idReserva}")
    public ResponseEntity<Reserva> buscarPorId(@PathVariable Integer idReserva) {
        return ResponseEntity.ok(reservaService.buscarPorId(idReserva));
    }

    @Operation(
            summary = "Generar reserva",
            description = "Permite a un cliente reservar un cupo en una clase especifica"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva generada correctamente"),
            @ApiResponse(responseCode = "400", description = "El cliente no esta activo o ya tiene una reserva para la clase"),
            @ApiResponse(responseCode = "404", description = "Cliente o clase no encontrados"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
   public ResponseEntity<Reserva> crearReserva(
            @Parameter(description = "ID del cliente que realiza la reserva", required = true)
            @RequestParam Integer clienteId,
            @Parameter(description = "ID de la clase a reservar", required = true)
            @RequestParam Integer claseId) {

        Reserva reserva = reservaService.generarReserva(clienteId, claseId);
        return ResponseEntity.status(201).body(reserva);
    }

    @Operation(
            summary = "Cancelar reserva",
            description = "Permite cancelar una reserva existente y liberar el cupo de la clase"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva cancelada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "400", description = "La reserva ya se encuentra cancelada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/cancelar/{idReserva}")
    public ResponseEntity<Reserva> cancelarReserva(@PathVariable Integer idReserva) {
        Reserva reservaCancelada = reservaService.cancelarReserva(idReserva);
        return ResponseEntity.ok(reservaCancelada);
    }

    @Operation(
            summary = "Eliminar reserva",
            description = "Elimina una reserva a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{idReserva}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer idReserva) {
        reservaService.eliminarReserva(idReserva);
        return ResponseEntity.noContent().build();
    }
}