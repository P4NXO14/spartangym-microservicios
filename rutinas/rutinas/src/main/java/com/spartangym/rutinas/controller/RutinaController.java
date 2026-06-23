package com.spartangym.rutinas.controller;

import com.spartangym.rutinas.model.Rutina;
import com.spartangym.rutinas.service.RutinaService;
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
@RequestMapping("/api/rutinas")
@RequiredArgsConstructor
@Tag(name = "Rutinas", description = "Gestion de las rutinas de entrenamiento asignadas a los clientes del gimnasio")
public class RutinaController {

    private final RutinaService rutinaService;

    @Operation(
            summary = "Listar rutinas",
            description = "Obtiene la lista con todas las rutinas registradas en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Rutina>> listarTodas() {
        return ResponseEntity.ok(rutinaService.listarTodas());
    }

    @Operation(
            summary = "Buscar rutina por ID",
            description = "Obtiene los datos de una rutina especifica a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutina encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Rutina no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Rutina> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(rutinaService.buscarPorId(id));
    }

    @Operation(
            summary = "Listar rutinas por cliente",
            description = "Obtiene todas las rutinas asignadas a un cliente especifico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Rutina>> listarPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(rutinaService.listarPorCliente(idCliente));
    }

    @Operation(
            summary = "Listar rutinas por dificultad",
            description = "Obtiene las rutinas filtradas por nivel de dificultad (por ejemplo: Principiante, Intermedio, Avanzado)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/dificultad/{dificultad}")
    public ResponseEntity<List<Rutina>> listarPorDificultad(@PathVariable String dificultad) {
        return ResponseEntity.ok(rutinaService.listarPorDificultad(dificultad));
    }

    @Operation(
            summary = "Registrar rutina",
            description = "Permite asignar una nueva rutina de entrenamiento a un cliente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rutina registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la rutina invalidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Rutina> guardar(@Valid @RequestBody Rutina rutina) {
        Rutina nuevaRutina = rutinaService.guardar(rutina);
        return ResponseEntity.status(201).body(nuevaRutina);
    }

    @Operation(
            summary = "Actualizar rutina",
            description = "Permite actualizar los datos de una rutina existente a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rutina actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Rutina no encontrada"),
            @ApiResponse(responseCode = "400", description = "Datos de la rutina invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Rutina> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Rutina rutina) {

        return ResponseEntity.ok(rutinaService.actualizar(id, rutina));
    }

    @Operation(
            summary = "Eliminar rutina",
            description = "Elimina una rutina a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rutina eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Rutina no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        rutinaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}