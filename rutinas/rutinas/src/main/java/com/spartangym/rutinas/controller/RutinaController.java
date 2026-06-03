package com.spartangym.rutinas.controller;

import com.spartangym.rutinas.model.Rutina;
import com.spartangym.rutinas.service.RutinaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rutinas")
@RequiredArgsConstructor
public class RutinaController {

    private final RutinaService rutinaService;

    @GetMapping
    public ResponseEntity<List<Rutina>> listarTodas() {
        return ResponseEntity.ok(rutinaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rutina> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(rutinaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Rutina>> listarPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(rutinaService.listarPorCliente(idCliente));
    }

    @GetMapping("/dificultad/{dificultad}")
    public ResponseEntity<List<Rutina>> listarPorDificultad(@PathVariable String dificultad) {
        return ResponseEntity.ok(rutinaService.listarPorDificultad(dificultad));
    }

    @PostMapping
    public ResponseEntity<Rutina> guardar(@Valid @RequestBody Rutina rutina) {
        Rutina nuevaRutina = rutinaService.guardar(rutina);
        return ResponseEntity.status(201).body(nuevaRutina);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rutina> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Rutina rutina) {

        return ResponseEntity.ok(rutinaService.actualizar(id, rutina));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        rutinaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}