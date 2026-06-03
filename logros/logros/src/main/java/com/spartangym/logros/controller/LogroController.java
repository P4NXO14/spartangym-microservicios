package com.spartangym.logros.controller;

import com.spartangym.logros.model.Logro;
import com.spartangym.logros.service.LogroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logros")
@RequiredArgsConstructor
public class LogroController {

    private final LogroService logroService;

    @GetMapping
    public ResponseEntity<List<Logro>> listarTodos() {
        return ResponseEntity.ok(logroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Logro> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(logroService.buscarPorId(id));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Logro>> listarPorCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(logroService.listarPorCliente(idCliente));
    }

    @PostMapping
    public ResponseEntity<Logro> guardar(@Valid @RequestBody Logro logro) {
        Logro nuevoLogro = logroService.guardar(logro);
        return ResponseEntity.status(201).body(nuevoLogro);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        logroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}