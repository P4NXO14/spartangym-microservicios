package com.spartangym.asistencia.controller;

import com.spartangym.asistencia.model.Asistencia;
import com.spartangym.asistencia.service.AsistenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asistencia")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    @GetMapping
    public List<Asistencia> listar() {
        return asistenciaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asistencia> buscar(@PathVariable Integer id) {
        return ResponseEntity.ok(asistenciaService.buscarPorId(id));
    }

    @GetMapping("/cliente/{idCliente}")
    public List<Asistencia> listarPorCliente(@PathVariable Integer idCliente) {
        return asistenciaService.listarPorCliente(idCliente);
    }

    @GetMapping("/estado/{estado}")
    public List<Asistencia> listarPorEstado(@PathVariable String estado) {
        return asistenciaService.listarPorEstado(estado);
    }

    @PostMapping("/crear")
    public ResponseEntity<Asistencia> registrar(@Valid @RequestBody Asistencia asistencia) {
        return ResponseEntity.status(201).body(asistenciaService.registrarIngreso(asistencia));
    }

    @PutMapping("/salida/{id}")
    public ResponseEntity<Asistencia> registrarSalida(@PathVariable Integer id) {
        return ResponseEntity.ok(asistenciaService.registrarSalida(id));
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Asistencia> cancelar(@PathVariable Integer id) {
        return ResponseEntity.ok(asistenciaService.cancelarAsistencia(id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}