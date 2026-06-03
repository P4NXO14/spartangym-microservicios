package com.spartangym.rutinas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "rutinas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rutina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRutina;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Column(nullable = false)
    private Integer idCliente;

    @NotBlank(message = "El nombre de la rutina es obligatorio")
    @Column(nullable = false)
    private String nombreRutina;

    @NotBlank(message = "La descripcion es obligatoria")
    @Column(nullable = false)
    private String descripcion;

    @NotBlank(message = "El objetivo es obligatorio")
    @Column(nullable = false)
    private String objetivo;

    @NotBlank(message = "La dificultad es obligatoria")
    @Column(nullable = false)
    private String dificultad;

    private LocalDate fechaAsignacion;
}