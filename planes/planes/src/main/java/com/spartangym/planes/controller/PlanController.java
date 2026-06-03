package com.spartangym.planes.controller;

import com.spartangym.planes.model.Plan;
import com.spartangym.planes.service.PlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planes")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public ResponseEntity<List<Plan>> listarTodos() {
        return ResponseEntity.ok(planService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(planService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Plan> guardar(@Valid @RequestBody Plan plan) {
        Plan nuevoPlan = planService.guardar(plan);
        return ResponseEntity.status(201).body(nuevoPlan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plan> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Plan plan) {

        return ResponseEntity.ok(planService.actualizar(id, plan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        planService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}