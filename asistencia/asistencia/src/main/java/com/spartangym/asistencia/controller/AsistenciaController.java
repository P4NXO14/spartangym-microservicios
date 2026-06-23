package com.spartangym.asistencia.controller;

import com.spartangym.asistencia.model.Asistencia;
import com.spartangym.asistencia.service.AsistenciaService;
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
@RequestMapping("/api/asistencia")
@RequiredArgsConstructor
@Tag(name = "Asistencia", description = "Registro y control de asistencia de los clientes del gimnasio")
public class AsistenciaController {

private final AsistenciaService asistenciaService;

    @Operation(
            summary = "Listar asistencias",
            description = "Obtiene la lista con todos los registros de asistencia"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public List<Asistencia> listar() {
        return asistenciaService.listarTodas();
    }

    @Operation(
            summary = "Buscar asistencia por ID",
            description = "Obtiene un registro de asistencia especifico a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asistencia encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Asistencia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Asistencia> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(asistenciaService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar asistencias por cliente",
            description = "Obtiene todos los registros de asistencia de un cliente especifico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/cliente/{idCliente}")
    public List<Asistencia> listarPorCliente(@PathVariable Integer idCliente) {
        return asistenciaService.listarPorCliente(idCliente);
    }

    @Operation(
            summary = "Listar asistencias por estado",
            description = "Obtiene los registros de asistencia filtrados por estado (REGISTRADA, COMPLETADA o CANCELADA)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "400", description = "Estado de asistencia no valido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/estado/{estado}")
    public List<Asistencia> listarPorEstado(@PathVariable String estado) {
        return asistenciaService.listarPorEstado(estado);
    }

    @Operation(
            summary = "Registrar ingreso",
            description = "Permite registrar el ingreso de un cliente al gimnasio"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Asistencia registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la asistencia invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/crear")
    public ResponseEntity<Asistencia> registrar(@Valid @RequestBody Asistencia asistencia) {
        return ResponseEntity.status(201).body(asistenciaService.registrarIngreso(asistencia));
    }

    @Operation(
            summary = "Registrar salida",
            description = "Permite registrar la salida de un cliente que tiene una asistencia en curso"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salida registrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Asistencia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/salida/{id}")
    public ResponseEntity<Asistencia> registrarSalida(@PathVariable Integer id) {
        return ResponseEntity.ok(asistenciaService.registrarSalida(id));
    }

    @Operation(
            summary = "Cancelar asistencia",
            description = "Permite cancelar un registro de asistencia existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Asistencia cancelada correctamente"),
            @ApiResponse(responseCode = "404", description = "Asistencia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Asistencia> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(asistenciaService.cancelarAsistencia(id));
    }

    @Operation(
            summary = "Eliminar asistencia",
            description = "Elimina un registro de asistencia a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Asistencia eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Asistencia no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}