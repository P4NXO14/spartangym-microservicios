package com.spartangym.planes.controller;

import com.spartangym.planes.model.Plan;
import com.spartangym.planes.service.PlanService;
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
@RequestMapping("/api/planes")
@RequiredArgsConstructor
@Tag(name = "Planes", description = "Gestion de los planes de membresia del gimnasio")
public class PlanController {

    private final PlanService planService;

    @Operation(
            summary = "Listar planes",
            description = "Obtiene la lista con todos los planes de membresia disponibles"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<Plan>> listarTodos() {
        return ResponseEntity.ok(planService.listarTodos());
    }

    @Operation(
            summary = "Buscar plan por ID",
            description = "Obtiene los datos de un plan especifico a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Plan> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(planService.buscarPorId(id));
    }

    @Operation(
            summary = "Registrar plan",
            description = "Permite registrar un nuevo plan de membresia"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plan registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del plan invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Plan> guardar(@Valid @RequestBody Plan plan) {
        Plan nuevoPlan = planService.guardar(plan);
        return ResponseEntity.status(201).body(nuevoPlan);
    }

    @Operation(
            summary = "Actualizar plan",
            description = "Permite actualizar los datos de un plan existente a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plan actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado"),
            @ApiResponse(responseCode = "400", description = "Datos del plan invalidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Plan> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Plan plan) {

        return ResponseEntity.ok(planService.actualizar(id, plan));
    }

    @Operation(
            summary = "Eliminar plan",
            description = "Elimina un plan de membresia a partir de su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plan eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Plan no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        planService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}