package com.spartangym.notificaciones.controller;

import com.spartangym.notificaciones.model.Notificacion;
import com.spartangym.notificaciones.service.NotificacionService;
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
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Envio y gestion de notificaciones a los clientes del gimnasio")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @Operation(
            summary = "Listar notificaciones",
            description = "Obtiene la lista con todas las notificaciones registradas en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Notificacion>> listarTodas() {
        return ResponseEntity.ok(notificacionService.listarTodas());
    }

    @Operation(
            summary = "Buscar notificacion por ID",
            description = "Obtiene los datos de una notificacion especifica a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificacion encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Notificacion no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(notificacionService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar notificaciones por cliente",
            description = "Obtiene todas las notificaciones enviadas a un cliente especifico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Notificacion>> listarPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(notificacionService.listarPorCliente(idCliente));
    }

    @Operation(
            summary = "Registrar notificacion",
            description = "Permite registrar y enviar una nueva notificacion a un cliente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Notificacion registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la notificacion invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Notificacion> guardar(@Valid @RequestBody Notificacion notificacion) {
        Notificacion nuevaNotificacion = notificacionService.guardar(notificacion);
        return ResponseEntity.status(201).body(nuevaNotificacion);
    }

    @Operation(
            summary = "Eliminar notificacion",
            description = "Elimina una notificacion a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Notificacion eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Notificacion no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        notificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}