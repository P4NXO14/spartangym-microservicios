package com.spartangym.rutinas.controller;

import com.spartangym.rutinas.dto.ErrorDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errores.put(fe.getField(), fe.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> handleDataIntegrity(DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorDTO("base de datos", "El registro ya existe en la base de datos."));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> handleRuntime(RuntimeException ex) {

        String mensaje = ex.getMessage().toLowerCase();

        if (mensaje.contains("no encontrado") ||
            mensaje.contains("no encontrada")) {

            return ResponseEntity.status(404)
                    .body(new ErrorDTO("error", ex.getMessage()));
        }

        if (mensaje.contains("microservicio de clientes") ||
            mensaje.contains("no se encuentra disponible") ||
            mensaje.contains("no esta disponible")) {

            return ResponseEntity.status(503)
                    .body(new ErrorDTO("servicio", ex.getMessage()));
        }

        return ResponseEntity.badRequest()
                .body(new ErrorDTO("error", ex.getMessage()));
    }
}