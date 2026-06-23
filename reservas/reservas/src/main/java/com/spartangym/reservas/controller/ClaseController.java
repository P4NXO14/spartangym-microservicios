package com.spartangym.reservas.controller;

import com.spartangym.reservas.model.Clase;
import com.spartangym.reservas.service.ClaseService;
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
@RequestMapping("/api/clases")
@RequiredArgsConstructor
@Tag(name = "Clases", description = "Gestion de las clases disponibles en el gimnasio")
public class ClaseController {

    private final ClaseService claseService;

    @Operation(
            summary = "Listar clases",
            description = "Obtiene la lista con todas las clases disponibles en el gimnasio"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Clase>> listar() {
        return ResponseEntity.ok(claseService.listarTodas());
    }

    @Operation(
            summary = "Buscar clase por ID",
            description = "Obtiene los datos de una clase especifica a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clase encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Clase no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Clase> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(claseService.buscarPorId(id));
    }

    @Operation(
            summary = "Registrar clase",
            description = "Permite registrar una nueva clase en el gimnasio"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Clase registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la clase invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Clase> crear(@Valid @RequestBody Clase clase) {
        Clase claseGuardada = claseService.guardar(clase);
        return ResponseEntity.status(201).body(claseGuardada);
    }

    @Operation(
            summary = "Actualizar clase",
            description = "Permite actualizar los datos de una clase existente a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clase actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Clase no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de la clase invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Clase> actualizar(@PathVariable Integer id,@Valid @RequestBody Clase clase) {
        return ResponseEntity.ok(claseService.actualizar(id, clase));
    }

    @Operation(
            summary = "Eliminar clase",
            description = "Elimina una clase a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Clase eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Clase no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        claseService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}