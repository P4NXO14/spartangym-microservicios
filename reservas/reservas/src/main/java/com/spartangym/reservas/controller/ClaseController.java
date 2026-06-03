package com.spartangym.reservas.controller;

import com.spartangym.reservas.model.Clase;
import com.spartangym.reservas.service.ClaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
@RequiredArgsConstructor
public class ClaseController {

    private final ClaseService claseService;

    @GetMapping
    public ResponseEntity<List<Clase>> listar() {
        return ResponseEntity.ok(claseService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clase> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(claseService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Clase> crear(@Valid @RequestBody Clase clase) {
        Clase claseGuardada = claseService.guardar(clase);
        return ResponseEntity.status(201).body(claseGuardada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clase> actualizar(@PathVariable Integer id,@Valid @RequestBody Clase clase) {
        return ResponseEntity.ok(claseService.actualizar(id, clase));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        claseService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}