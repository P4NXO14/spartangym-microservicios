package com.spartangym.logros.controller;

import com.spartangym.logros.model.Logro;
import com.spartangym.logros.service.LogroService;
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
@RequestMapping("/api/logros")
@RequiredArgsConstructor
@Tag(name = "Logros", description = "Gestion de los logros obtenidos por los clientes del gimnasio")
public class LogroController {

    private final LogroService logroService;

    @Operation(
            summary = "Listar logros",
            description = "Obtiene la lista con todos los logros registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Logro>> listarTodos() {
        return ResponseEntity.ok(logroService.listarTodos());
    }

    @Operation(
            summary = "Buscar logro por ID",
            description = "Obtiene los datos de un logro especifico a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logro encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Logro no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Logro> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(logroService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar logros por cliente",
            description = "Obtiene todos los logros obtenidos por un cliente especifico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Logro>> listarPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(logroService.listarPorCliente(idCliente));
    }

    @Operation(
            summary = "Registrar logro",
            description = "Permite registrar un nuevo logro para un cliente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Logro registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del logro invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Logro> guardar(@Valid @RequestBody Logro logro) {
        Logro nuevoLogro = logroService.guardar(logro);
        return ResponseEntity.status(201).body(nuevoLogro);
    }

    @Operation(
            summary = "Eliminar logro",
            description = "Elimina un logro a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Logro eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Logro no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        logroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}